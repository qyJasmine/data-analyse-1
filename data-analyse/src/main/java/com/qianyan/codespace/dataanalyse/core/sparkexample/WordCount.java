package com.qianyan.codespace.dataanalyse.core.sparkexample;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class WordCount {

    private static final Logger LOG = LoggerFactory.getLogger(WordCount.class);
    private static final Pattern SPACE = Pattern.compile("\\s+");

    public static void main(String[] args) throws Exception {

        // data source : local log file
        String filePath = "F:\\LdHook.log";

        // get spark session, must specify master
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWordCount")
                .master("local")
                .getOrCreate();

        // read lines from file
        JavaRDD<String> lines = spark.read().textFile(filePath).javaRDD();
        LOG.info("JavaRDD size: {}", lines.count());

        JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());

        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);

        List<Tuple2<String, Integer>> output = counts.collect();
//        for (Tuple2<?,?> tuple : output) {
//            System.out.println(tuple._1() + ", count: " + tuple._2());
//        }
        spark.stop();
    }
}
