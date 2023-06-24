import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {
    
    // Define grammar check pattern
    private static final String GRAMMAR_PATTERN = "((.*)([\\.\\?\\!]+)(.*)\\s+(([A-Z][a-z]+)[\\s ])*([A-Z][a-z]+)([\\.\\?\\!]+))";
    private static final Pattern pattern = Pattern.compile(GRAMMAR_PATTERN, Pattern.MULTILINE | Pattern.DOTALL);
    
    // Define plagiarism detection threshold
    private static final int PLAGIARISM_THRESHOLD = 80;
    
    // Define word translator
    private static final Translator translator = new Translator();
    
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            
            while (tokenizer.hasMoreTokens()) {
                String originalWord = tokenizer.nextToken();
                
                // Translate word to another language
                String translatedWord = translator.translate(originalWord);
                
                word.set(translatedWord);
                
                // Check if translated word matches grammar check pattern
                Matcher matcher = pattern.matcher(word.toString());
                boolean isCorrectGrammar = matcher.matches();
                
                // Check for plagiarism
                String valueString = value.toString();
                valueString = valueString.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
                String[] valueStringArray = valueString.split(" ");
                String[] previousLineArray = context.getConfiguration().get("previousLine").replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase().split(" ");
                double similarity = getSimilarity(valueStringArray, previousLineArray);
                boolean isOriginal = similarity < PLAGIARISM_THRESHOLD;
                
                if (isCorrectGrammar && isOriginal) {
                    context.write(word, one);
                }
            }
            // Save current line in context for plagiarism detection
            context.getConfiguration().set("previousLine", value.toString());
        }
        
        private double getSimilarity(String[] valueStringArray, String[] previousLineArray) {
            int sizeA = valueStringArray.length;
            int sizeB = previousLineArray.length;
            int intersectionSize = 0;
            
            for (String wordA : valueStringArray) {
                for (String wordB : previousLineArray) {
                    if (wordA.equals(wordB)) {
                        intersectionSize++;
                        break;
                    }
                }
            }
            
            double AunionB = sizeA + sizeB - intersectionSize;
            return (double) intersectionSize / AunionB * 100;
        }
    }
    
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

public class Translator {
    
    // Implement translation logic here
    public String translate(String text) {
        // Your translation code goes here
        return translatedText;
    }
}

public class WordFrequency {
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word frequency");
        job.setJarByClass(WordFrequency.class);
        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
