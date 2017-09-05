package io.github.alphacalculus.alphacalculus;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;


public class LearningLog {
    private SharedPreferences sharedPref;
    private int[] part_max_learned = new int[3];

    public LearningLog() {
        Context context = TheApp.getInstance().getApplicationContext();
        sharedPref = context.getSharedPreferences("io.github.alphacalculus.learned_chapters", Context.MODE_PRIVATE);
        part_max_learned[1] = sharedPref.getInt("max_1", -1);
        part_max_learned[2] = sharedPref.getInt("max_2", -1);
    }

    /**
     * Set chapter of part as learned.
     * @param part
     * @param chapter
     */
    public void doLearn(int part, int chapter) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(makeKey(part,chapter), true);
        switch (part) {
            case 1:
            case 2:
                if (part_max_learned[part] < chapter) {
                    part_max_learned[part] = chapter;
                    editor.putInt("max_"+Integer.toString(part), chapter);
                    editor.putLong("max_date_"+Integer.toString(part), System.currentTimeMillis());
                }
                break;
        }
        for (OnLearnedListener listener:listeners) {
            listener.onLearned(part,chapter);
        }
        editor.commit();
    }

    /**
     * Check if chapter of part is learned.
     * @param part
     * @param chapter
     * @return
     */
    public boolean isLearned(int part, int chapter) {
        return sharedPref.getBoolean(makeKey(part,chapter), false);
    }
    public boolean isLearned(ChapterItem chapter) {
        return isLearned(chapter.getPartIdx(),chapter.getChapterIdx());
    }

    // Utils

    public String makeKey(int part, int chapter) {
        return Integer.toString(part)+"_"+Integer.toString(chapter);
    }

    interface OnLearnedListener {
        void onLearned(int part, int chapter);
    }

    private ArrayList<OnLearnedListener> listeners = new ArrayList<OnLearnedListener>();
    public void register(OnLearnedListener listener) {
        listeners.add(listener);
    }
}
