package io.github.alphacalculus.alphacalculus;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextViewWithImages extends AppCompatTextView {

    public TextViewWithImages(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextViewWithImages(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWithImages(Context context) {
        super(context);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        Spannable s = getTextWithImages(this, text);
        super.setMovementMethod(LinkMovementMethod.getInstance());
        super.setText(s, BufferType.SPANNABLE);
    }

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    private static boolean addImages(TextViewWithImages self, Spannable spannable) {
        Context context = self.getContext();
        final Activity parent = self.getActivity();
        Pattern refImg = Pattern.compile("\\Q[img\\E\\s+?src=([a-zA-Z0-9_]+?)/\\Q]\\E");
        boolean hasChanges = false;

        Matcher matcher = refImg.matcher(spannable);
        while (matcher.find()) {
            boolean set = true;
            for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                if (spannable.getSpanStart(span) >= matcher.start()
                        && spannable.getSpanEnd(span) <= matcher.end()
                        ) {
                    spannable.removeSpan(span);
                } else {
                    set = false;
                    break;
                }
            }
            final String resname = spannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim();
            final int id = context.getResources().getIdentifier(resname, "drawable", context.getPackageName());
            if (id==0) {
                continue;
            }
            if (set) {
                hasChanges = true;
                Drawable d = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    d = context.getDrawable(id);
                } else {
                    d = context.getResources().getDrawable(id);
                }
                spannable.setSpan(new ResizeImageSpan(d,self.getWidth()),
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                spannable.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(parent, ImageViewActivity.class);
                        intent.putExtra("io.github.alphacalculus.IMAGE_SHOW_ID", id);
                        parent.startActivity(intent);
                    }
                }, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return hasChanges;
    }

    private Activity getActivity() {
        Context context = getContext();
        if (context instanceof Activity) {
            return (Activity) context;
        }
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private static Spannable getTextWithImages(TextViewWithImages t, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addImages(t, spannable);
        return spannable;
    }
}