package pers.spj.custom.view.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * @author bluefox
 * @date 2023/7/20.
 */

public class FontManager {
   private static Typeface normal;
   private static Typeface bold;
   private static Typeface italic;
   private static Typeface mRegular;

   private static String mBoldPath=null;
   private static String mItalicPath=null;
   private static String mNormalPath=null;
   private static String mRegularPath=null;
   private static final class FontManagerHolder {
      private static FontManager mFontManager = new FontManager();
   }
   private FontManager(){}
   public static FontManager getInstance() {
      return FontManagerHolder.mFontManager;
   }

   public FontManager setmBoldPath(String boldPath) {
      FontManager.mBoldPath = boldPath;
      return this;
   }

   public FontManager setmItalicPath(String italicPath) {
      FontManager.mItalicPath = italicPath;
      return this;
   }

   public  FontManager setmNormalPath(String normalPath) {
      FontManager.mNormalPath = normalPath;
      return this;
   }

   public  FontManager setmRegularPath(String regularPath) {
      FontManager.mRegularPath = regularPath;
      return this;
   }
   public void initFont(@NonNull Context context){
      getItalic(context);
      getBold(context);
      getRegular(context);
      getNormal(context);
   }

   public  void applyTypeface(@NonNull TextView v) {
      //disabled custom font
      if (v.getTypeface() == null) {
         v.setTypeface(getNormal(v.getContext()));
         return;
      }
      switch (v.getTypeface().getStyle()) {
         case Typeface.BOLD:
            v.setTypeface(getBold(v.getContext()));
            v.getPaint().setFakeBoldText(true);
            break;
         case Typeface.NORMAL:
            v.setTypeface(getNormal(v.getContext()));
            break;
         case Typeface.ITALIC:
            v.setTypeface(getItalic(v.getContext()));
            v.getPaint().setTextSkewX(-0.25f);
            break;
         default:
            break;
      }
   }

   public  void applyButtonTypeface(@NonNull Button v) {
      //disabled custom font
      if (v.getTypeface() == null) {
         v.setTypeface(getNormal(v.getContext()));
         return;
      }
      switch (v.getTypeface().getStyle()) {
         case Typeface.BOLD:
            v.setTypeface(getBold(v.getContext()));
            v.getPaint().setFakeBoldText(true);
            break;
         case Typeface.NORMAL:
            v.setTypeface(getNormal(v.getContext()));
            break;
         case Typeface.ITALIC:
            v.setTypeface(getItalic(v.getContext()));
            v.getPaint().setTextSkewX(-0.25f);
            break;
//            case Typeface.BOLD_ITALIC:
//                v.setTypeface(getBoldItalic(v.getContext()));
//                v.getPaint().setFakeBoldText(true);
//                v.getPaint().setTextSkewX(-0.25f);
//                break;
         default:
            break;
      }

   }

   public  void applyTypeface(@NonNull Context context, Paint v) {
      if (v.getTypeface() == null) {
         v.setTypeface(getNormal(context));
         return;
      }
      switch (v.getTypeface().getStyle()) {
         case Typeface.BOLD:
            v.setTypeface(getBold(context));
            v.setFakeBoldText(true);
            break;
         case Typeface.NORMAL:
            v.setTypeface(getNormal(context));
            break;
         case Typeface.ITALIC:
            v.setTypeface(getItalic(context));
            break;
//            case Typeface.BOLD_ITALIC:
//                v.setTypeface(getBoldItalic(context));
//                v.setFakeBoldText(true);
//                break;
         default:
            break;
      }
   }

   private synchronized static Typeface getNormal(@NonNull Context context) {
      if (normal == null) {
         normal = loadFont(context.getApplicationContext().getAssets(), mNormalPath);
      }
      return normal;
   }

   private synchronized static Typeface getBold(@NonNull Context context) {
      if (bold == null) {
         bold = loadFont(context.getApplicationContext().getAssets(), mBoldPath);
      }
      return bold;
   }

   private synchronized static Typeface getItalic(@NonNull Context context) {
      if (italic == null) {
         italic = loadFont(context.getApplicationContext().getAssets(), mItalicPath);
      }
      return italic;
   }


   private synchronized static Typeface getRegular(@NonNull Context context) {
      if (mRegular == null) {
         mRegular = loadFont(context.getApplicationContext().getAssets(), mRegularPath);
      }
      return mRegular;
   }
   private static Typeface loadFont(@NonNull AssetManager am, String path) {
      if(path==null){
         return Typeface.DEFAULT;
      }
      try {
         Typeface tf = Typeface.createFromAsset(am, path);
         return tf;
      } catch (Exception e) {
         return Typeface.DEFAULT;
      }
   }

}
