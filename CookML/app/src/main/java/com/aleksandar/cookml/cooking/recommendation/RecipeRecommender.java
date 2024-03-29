package com.aleksandar.cookml.cooking.recommendation;

import android.content.res.AssetManager;
import com.aleksandar.cookml.cooking.recommendation.interfaces.IRecommender;
import com.aleksandar.cookml.models.Ingredient;
import com.aleksandar.cookml.models.Recipe;

import java.util.*;


public class RecipeRecommender implements IRecommender {
    private List<Recipe> recipes = new ArrayList<Recipe>();
    private String[] recipeCorpus;
    private int recipeCorpusSize;
    private int[] recipeSizes;

    private final String RECIPES_JSON = "recipes.json";
    private final Integer N = 10;

    public RecipeRecommender() {}

    public RecipeRecommender(final AssetManager assetManager) {
        loadRecipes(assetManager);
    }

    public int getTokensAmmount(String d) {
        boolean inToken = false;
        int tokensAmmount = 0;

        //gather up potential tokens
        int end = d.length();
        for (int i = 0; i < end; i++) {
            if (Character.isWhitespace(d.charAt(i))  ||
                    Character.getType(d.charAt(i)) == Character.SPACE_SEPARATOR) {
                if (inToken) {
                    tokensAmmount += 1;
                    inToken = false;
                }
            }
            else {
                inToken = true;
            }
        }

        if (inToken) {
            tokensAmmount += 1;
        }

        return tokensAmmount;
    }

    private void loadRecipes(final AssetManager assetManager) {
        recipes = RecipeJSONParser.parse(assetManager, RECIPES_JSON);

        recipeCorpus = new String[recipes.size()];
        recipeSizes = new int[recipes.size()];
        recipeCorpusSize = 0;

        for(int i = 0; i < recipeCorpus.length; i++) {
            recipeCorpus[i] = recipes.get(i).toString();
            int doc_length = getTokensAmmount(recipeCorpus[i]);
            recipeCorpusSize += doc_length;
            recipeSizes[i] = doc_length;
        }
    }

    public List<Recipe> recommend(final AssetManager assetManager, List<Ingredient> ingredients) {
        if(recipes.size() == 0) {
            loadRecipes(assetManager);
        }

        return recommend(ingredients);
    }

    @Override
    public List<Recipe> recommend(List<Ingredient> ingredients) {
        float[][] tfidf = new float[ingredients.size()][recipeCorpus.length];
        float[] queryVector = new float[ingredients.size()];

        // Calculate the TF-IDF values
        for(int i = 0; i < ingredients.size(); i++) {
            String ingredient = ingredients.get(i).name;

            float allOccurences = 0;

            for(int j = 0; j < recipeCorpus.length; j++) {
                String document = recipeCorpus[j];

                int occurences = count(document, ingredient);

                tfidf[i][j] = occurences * 1f / recipeSizes[j];
                allOccurences += occurences;
            }

            float idf = 0;
            if(allOccurences > 0) {
                idf = (float) Math.log(recipeCorpusSize / allOccurences);
            }
            queryVector[i] = (1f / ingredients.size()) * idf;
            for(int j = 0; j < recipeSizes.length; j++) {
                tfidf[i][j] = tfidf[i][j] * idf;
            }
        }

        // Calculate the cosine distance between the queryVector and each recipe vector
        Map<Integer, Float> cosineDistances = calculateCosineDistances(tfidf, queryVector);
        List<Integer> topN = getTopN(cosineDistances, N);

        ArrayList<Recipe> topRecipes = new ArrayList<Recipe>();

        for(Integer index : topN) {
            topRecipes.add(recipes.get(index));
        }

        return topRecipes;
    }

    private List<Integer> getTopN(final Map<Integer, Float> map, int n) {
        PriorityQueue<Integer> topN = new PriorityQueue<Integer>(n, new Comparator<Integer>() {
            public int compare(Integer s1, Integer s2) {
                return Float.compare(map.get(s1), map.get(s2));
            }
        });

        for(Integer key : map.keySet()){
            if (topN.size() < n)
                topN.add(key);
            else if (map.get(topN.peek()) < map.get(key)) {
                topN.poll();
                topN.add(key);
            }
        }
        return (List) Arrays.asList(topN.toArray());
    }

    private Map<Integer, Float> calculateCosineDistances(float[][] tfidf, float[] queryVector) {
        Map<Integer, Float> cosineDistances = new TreeMap<Integer, Float>();

        for(int j = 0; j < tfidf[0].length; j++) {
            float sum = 0f;
            float sumRecipeVector = 0f;
            float sumQueryVector = 0f;
            for(int i = 0; i < tfidf.length; i++) {
                sum += tfidf[i][j]*queryVector[i];
                sumRecipeVector += (float) Math.pow(tfidf[i][j], 2);
                sumQueryVector += (float) Math.pow(queryVector[i], 2);
            }
            float cosineDistance = 0;
            if(sumRecipeVector > 0) {
                cosineDistance = sum / ((float) Math.sqrt(sumQueryVector) * (float) Math.sqrt(sumRecipeVector));
            }

            cosineDistances.put(j, cosineDistance);
        }

        return cosineDistances;
    }

    public int count(String text, String find) {
        int index = 0, count = 0, length = find.length();
        while( (index = text.indexOf(find, index)) != -1 ) {
            index += length; count++;
        }
        return count;
    }
}
