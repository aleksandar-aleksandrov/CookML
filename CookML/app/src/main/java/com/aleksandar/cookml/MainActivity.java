package com.aleksandar.cookml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;
import opennlp.tools.doccat.NGramFeatureGenerator;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startCooking(View view) throws InvalidFormatException {
        Tokenizer t = new SimpleTokenizer();
        List<String> a = Arrays.asList(t.tokenize("lllll abev"));
        for(String w : a) {
            System.out.println(w);
        }
        String[] text = {
                "This is the very first sentence.",
                "This is the second sentence"
        };
        NGramFeatureGenerator g = new NGramFeatureGenerator();
        Collection<String> col = g.extractFeatures(text, new HashMap<String, Object>());
        for(String p : col) {
            System.out.println(p);
        }
        Intent myIntent = new Intent(this, IngredientRecognitionActivity.class);
        startActivity(myIntent);
    }

}
