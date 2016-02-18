package com.cloud.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
	private static HashMap<String, Integer> htmlEntities = new HashMap<String, Integer>();
	static {
		htmlEntities.put("nbsp", Integer.valueOf(160));
		htmlEntities.put("iexcl", Integer.valueOf(161));
		htmlEntities.put("cent", Integer.valueOf(162));
		htmlEntities.put("pound", Integer.valueOf(163));
		htmlEntities.put("curren", Integer.valueOf(164));
		htmlEntities.put("yen", Integer.valueOf(165));
		htmlEntities.put("brvbar", Integer.valueOf(166));
		htmlEntities.put("sect", Integer.valueOf(167));
		htmlEntities.put("uml", Integer.valueOf(168));
		htmlEntities.put("copy", Integer.valueOf(169));
		htmlEntities.put("ordf", Integer.valueOf(170));
		htmlEntities.put("laquo", Integer.valueOf(171));
		htmlEntities.put("not", Integer.valueOf(172));
		htmlEntities.put("shy", Integer.valueOf(173));
		htmlEntities.put("reg", Integer.valueOf(174));
		htmlEntities.put("macr", Integer.valueOf(175));
		htmlEntities.put("deg", Integer.valueOf(176));
		htmlEntities.put("plusmn", Integer.valueOf(177));
		htmlEntities.put("sup2", Integer.valueOf(178));
		htmlEntities.put("sup3", Integer.valueOf(179));
		htmlEntities.put("acute", Integer.valueOf(180));
		htmlEntities.put("micro", Integer.valueOf(181));
		htmlEntities.put("para", Integer.valueOf(182));
		htmlEntities.put("middot", Integer.valueOf(183));
		htmlEntities.put("cedil", Integer.valueOf(184));
		htmlEntities.put("sup1", Integer.valueOf(185));
		htmlEntities.put("ordm", Integer.valueOf(186));
		htmlEntities.put("raquo", Integer.valueOf(187));
		htmlEntities.put("frac14", Integer.valueOf(188));
		htmlEntities.put("frac12", Integer.valueOf(189));
		htmlEntities.put("frac34", Integer.valueOf(190));
		htmlEntities.put("iquest", Integer.valueOf(191));
		htmlEntities.put("Agrave", Integer.valueOf(192));
		htmlEntities.put("Aacute", Integer.valueOf(193));
		htmlEntities.put("Acirc", Integer.valueOf(194));
		htmlEntities.put("Atilde", Integer.valueOf(195));
		htmlEntities.put("Auml", Integer.valueOf(196));
		htmlEntities.put("Aring", Integer.valueOf(197));
		htmlEntities.put("AElig", Integer.valueOf(198));
		htmlEntities.put("Ccedil", Integer.valueOf(199));
		htmlEntities.put("Egrave", Integer.valueOf(200));
		htmlEntities.put("Eacute", Integer.valueOf(201));
		htmlEntities.put("Ecirc", Integer.valueOf(202));
		htmlEntities.put("Euml", Integer.valueOf(203));
		htmlEntities.put("Igrave", Integer.valueOf(204));
		htmlEntities.put("Iacute", Integer.valueOf(205));
		htmlEntities.put("Icirc", Integer.valueOf(206));
		htmlEntities.put("Iuml", Integer.valueOf(207));
		htmlEntities.put("ETH", Integer.valueOf(208));
		htmlEntities.put("Ntilde", Integer.valueOf(209));
		htmlEntities.put("Ograve", Integer.valueOf(210));
		htmlEntities.put("Oacute", Integer.valueOf(211));
		htmlEntities.put("Ocirc", Integer.valueOf(212));
		htmlEntities.put("Otilde", Integer.valueOf(213));
		htmlEntities.put("Ouml", Integer.valueOf(214));
		htmlEntities.put("times", Integer.valueOf(215));
		htmlEntities.put("Oslash", Integer.valueOf(216));
		htmlEntities.put("Ugrave", Integer.valueOf(217));
		htmlEntities.put("Uacute", Integer.valueOf(218));
		htmlEntities.put("Ucirc", Integer.valueOf(219));
		htmlEntities.put("Uuml", Integer.valueOf(220));
		htmlEntities.put("Yacute", Integer.valueOf(221));
		htmlEntities.put("THORN", Integer.valueOf(222));
		htmlEntities.put("szlig", Integer.valueOf(223));
		htmlEntities.put("agrave", Integer.valueOf(224));
		htmlEntities.put("aacute", Integer.valueOf(225));
		htmlEntities.put("acirc", Integer.valueOf(226));
		htmlEntities.put("atilde", Integer.valueOf(227));
		htmlEntities.put("auml", Integer.valueOf(228));
		htmlEntities.put("aring", Integer.valueOf(229));
		htmlEntities.put("aelig", Integer.valueOf(230));
		htmlEntities.put("ccedil", Integer.valueOf(231));
		htmlEntities.put("egrave", Integer.valueOf(232));
		htmlEntities.put("eacute", Integer.valueOf(233));
		htmlEntities.put("ecirc", Integer.valueOf(234));
		htmlEntities.put("euml", Integer.valueOf(235));
		htmlEntities.put("igrave", Integer.valueOf(236));
		htmlEntities.put("iacute", Integer.valueOf(237));
		htmlEntities.put("icirc", Integer.valueOf(238));
		htmlEntities.put("iuml", Integer.valueOf(239));
		htmlEntities.put("eth", Integer.valueOf(240));
		htmlEntities.put("ntilde", Integer.valueOf(241));
		htmlEntities.put("ograve", Integer.valueOf(242));
		htmlEntities.put("oacute", Integer.valueOf(243));
		htmlEntities.put("ocirc", Integer.valueOf(244));
		htmlEntities.put("otilde", Integer.valueOf(245));
		htmlEntities.put("ouml", Integer.valueOf(246));
		htmlEntities.put("divide", Integer.valueOf(247));
		htmlEntities.put("oslash", Integer.valueOf(248));
		htmlEntities.put("ugrave", Integer.valueOf(249));
		htmlEntities.put("uacute", Integer.valueOf(250));
		htmlEntities.put("ucirc", Integer.valueOf(251));
		htmlEntities.put("uuml", Integer.valueOf(252));
		htmlEntities.put("yacute", Integer.valueOf(253));
		htmlEntities.put("thorn", Integer.valueOf(254));
		htmlEntities.put("yuml", Integer.valueOf(255));
		htmlEntities.put("fnof", Integer.valueOf(402));
		htmlEntities.put("Alpha", Integer.valueOf(913));
		htmlEntities.put("Beta", Integer.valueOf(914));
		htmlEntities.put("Gamma", Integer.valueOf(915));
		htmlEntities.put("Delta", Integer.valueOf(916));
		htmlEntities.put("Epsilon", Integer.valueOf(917));
		htmlEntities.put("Zeta", Integer.valueOf(918));
		htmlEntities.put("Eta", Integer.valueOf(919));
		htmlEntities.put("Theta", Integer.valueOf(920));
		htmlEntities.put("Iota", Integer.valueOf(921));
		htmlEntities.put("Kappa", Integer.valueOf(922));
		htmlEntities.put("Lambda", Integer.valueOf(923));
		htmlEntities.put("Mu", Integer.valueOf(924));
		htmlEntities.put("Nu", Integer.valueOf(925));
		htmlEntities.put("Xi", Integer.valueOf(926));
		htmlEntities.put("Omicron", Integer.valueOf(927));
		htmlEntities.put("Pi", Integer.valueOf(928));
		htmlEntities.put("Rho", Integer.valueOf(929));
		htmlEntities.put("Sigma", Integer.valueOf(931));
		htmlEntities.put("Tau", Integer.valueOf(932));
		htmlEntities.put("Upsilon", Integer.valueOf(933));
		htmlEntities.put("Phi", Integer.valueOf(934));
		htmlEntities.put("Chi", Integer.valueOf(935));
		htmlEntities.put("Psi", Integer.valueOf(936));
		htmlEntities.put("Omega", Integer.valueOf(937));
		htmlEntities.put("alpha", Integer.valueOf(945));
		htmlEntities.put("beta", Integer.valueOf(946));
		htmlEntities.put("gamma", Integer.valueOf(947));
		htmlEntities.put("delta", Integer.valueOf(948));
		htmlEntities.put("epsilon", Integer.valueOf(949));
		htmlEntities.put("zeta", Integer.valueOf(950));
		htmlEntities.put("eta", Integer.valueOf(951));
		htmlEntities.put("theta", Integer.valueOf(952));
		htmlEntities.put("iota", Integer.valueOf(953));
		htmlEntities.put("kappa", Integer.valueOf(954));
		htmlEntities.put("lambda", Integer.valueOf(955));
		htmlEntities.put("mu", Integer.valueOf(956));
		htmlEntities.put("nu", Integer.valueOf(957));
		htmlEntities.put("xi", Integer.valueOf(958));
		htmlEntities.put("omicron", Integer.valueOf(959));
		htmlEntities.put("pi", Integer.valueOf(960));
		htmlEntities.put("rho", Integer.valueOf(961));
		htmlEntities.put("sigmaf", Integer.valueOf(962));
		htmlEntities.put("sigma", Integer.valueOf(963));
		htmlEntities.put("tau", Integer.valueOf(964));
		htmlEntities.put("upsilon", Integer.valueOf(965));
		htmlEntities.put("phi", Integer.valueOf(966));
		htmlEntities.put("chi", Integer.valueOf(967));
		htmlEntities.put("psi", Integer.valueOf(968));
		htmlEntities.put("omega", Integer.valueOf(969));
		htmlEntities.put("thetasym", Integer.valueOf(977));
		htmlEntities.put("upsih", Integer.valueOf(978));
		htmlEntities.put("piv", Integer.valueOf(982));
		htmlEntities.put("bull", Integer.valueOf(8226));
		htmlEntities.put("hellip", Integer.valueOf(8230));
		htmlEntities.put("prime", Integer.valueOf(8242));
		htmlEntities.put("Prime", Integer.valueOf(8243));
		htmlEntities.put("oline", Integer.valueOf(8254));
		htmlEntities.put("frasl", Integer.valueOf(8260));
		htmlEntities.put("weierp", Integer.valueOf(8472));
		htmlEntities.put("image", Integer.valueOf(8465));
		htmlEntities.put("real", Integer.valueOf(8476));
		htmlEntities.put("trade", Integer.valueOf(8482));
		htmlEntities.put("alefsym", Integer.valueOf(8501));
		htmlEntities.put("larr", Integer.valueOf(8592));
		htmlEntities.put("uarr", Integer.valueOf(8593));
		htmlEntities.put("rarr", Integer.valueOf(8594));
		htmlEntities.put("darr", Integer.valueOf(8595));
		htmlEntities.put("harr", Integer.valueOf(8596));
		htmlEntities.put("crarr", Integer.valueOf(8629));
		htmlEntities.put("lArr", Integer.valueOf(8656));
		htmlEntities.put("uArr", Integer.valueOf(8657));
		htmlEntities.put("rArr", Integer.valueOf(8658));
		htmlEntities.put("dArr", Integer.valueOf(8659));
		htmlEntities.put("hArr", Integer.valueOf(8660));
		htmlEntities.put("forall", Integer.valueOf(8704));
		htmlEntities.put("part", Integer.valueOf(8706));
		htmlEntities.put("exist", Integer.valueOf(8707));
		htmlEntities.put("empty", Integer.valueOf(8709));
		htmlEntities.put("nabla", Integer.valueOf(8711));
		htmlEntities.put("isin", Integer.valueOf(8712));
		htmlEntities.put("notin", Integer.valueOf(8713));
		htmlEntities.put("ni", Integer.valueOf(8715));
		htmlEntities.put("prod", Integer.valueOf(8719));
		htmlEntities.put("sum", Integer.valueOf(8721));
		htmlEntities.put("minus", Integer.valueOf(8722));
		htmlEntities.put("lowast", Integer.valueOf(8727));
		htmlEntities.put("radic", Integer.valueOf(8730));
		htmlEntities.put("prop", Integer.valueOf(8733));
		htmlEntities.put("infin", Integer.valueOf(8734));
		htmlEntities.put("ang", Integer.valueOf(8736));
		htmlEntities.put("and", Integer.valueOf(8743));
		htmlEntities.put("or", Integer.valueOf(8744));
		htmlEntities.put("cap", Integer.valueOf(8745));
		htmlEntities.put("cup", Integer.valueOf(8746));
		htmlEntities.put("int", Integer.valueOf(8747));
		htmlEntities.put("there4", Integer.valueOf(8756));
		htmlEntities.put("sim", Integer.valueOf(8764));
		htmlEntities.put("cong", Integer.valueOf(8773));
		htmlEntities.put("asymp", Integer.valueOf(8776));
		htmlEntities.put("ne", Integer.valueOf(8800));
		htmlEntities.put("equiv", Integer.valueOf(8801));
		htmlEntities.put("le", Integer.valueOf(8804));
		htmlEntities.put("ge", Integer.valueOf(8805));
		htmlEntities.put("sub", Integer.valueOf(8834));
		htmlEntities.put("sup", Integer.valueOf(8835));
		htmlEntities.put("nsub", Integer.valueOf(8836));
		htmlEntities.put("sube", Integer.valueOf(8838));
		htmlEntities.put("supe", Integer.valueOf(8839));
		htmlEntities.put("oplus", Integer.valueOf(8853));
		htmlEntities.put("otimes", Integer.valueOf(8855));
		htmlEntities.put("perp", Integer.valueOf(8869));
		htmlEntities.put("sdot", Integer.valueOf(8901));
		htmlEntities.put("lceil", Integer.valueOf(8968));
		htmlEntities.put("rceil", Integer.valueOf(8969));
		htmlEntities.put("lfloor", Integer.valueOf(8970));
		htmlEntities.put("rfloor", Integer.valueOf(8971));
		htmlEntities.put("lang", Integer.valueOf(9001));
		htmlEntities.put("rang", Integer.valueOf(9002));
		htmlEntities.put("loz", Integer.valueOf(9674));
		htmlEntities.put("spades", Integer.valueOf(9824));
		htmlEntities.put("clubs", Integer.valueOf(9827));
		htmlEntities.put("hearts", Integer.valueOf(9829));
		htmlEntities.put("diams", Integer.valueOf(9830));
		htmlEntities.put("quot", Integer.valueOf(34));
		htmlEntities.put("amp", Integer.valueOf(38));
		htmlEntities.put("lt", Integer.valueOf(60));
		htmlEntities.put("gt", Integer.valueOf(62));
		htmlEntities.put("OElig", Integer.valueOf(338));
		htmlEntities.put("oelig", Integer.valueOf(339));
		htmlEntities.put("Scaron", Integer.valueOf(352));
		htmlEntities.put("scaron", Integer.valueOf(353));
		htmlEntities.put("Yuml", Integer.valueOf(376));
		htmlEntities.put("circ", Integer.valueOf(710));
		htmlEntities.put("tilde", Integer.valueOf(732));
		htmlEntities.put("ensp", Integer.valueOf(8194));
		htmlEntities.put("emsp", Integer.valueOf(8195));
		htmlEntities.put("thinsp", Integer.valueOf(8201));
		htmlEntities.put("zwnj", Integer.valueOf(8204));
		htmlEntities.put("zwj", Integer.valueOf(8205));
		htmlEntities.put("lrm", Integer.valueOf(8206));
		htmlEntities.put("rlm", Integer.valueOf(8207));
		htmlEntities.put("ndash", Integer.valueOf(8211));
		htmlEntities.put("mdash", Integer.valueOf(8212));
		htmlEntities.put("lsquo", Integer.valueOf(8216));
		htmlEntities.put("rsquo", Integer.valueOf(8217));
		htmlEntities.put("sbquo", Integer.valueOf(8218));
		htmlEntities.put("ldquo", Integer.valueOf(8220));
		htmlEntities.put("rdquo", Integer.valueOf(8221));
		htmlEntities.put("bdquo", Integer.valueOf(8222));
		htmlEntities.put("dagger", Integer.valueOf(8224));
		htmlEntities.put("Dagger", Integer.valueOf(8225));
		htmlEntities.put("permil", Integer.valueOf(8240));
		htmlEntities.put("lsaquo", Integer.valueOf(8249));
		htmlEntities.put("rsaquo", Integer.valueOf(8250));
		htmlEntities.put("euro", Integer.valueOf(8364));
	}
	
	public static final String unescapeHTML(String source) {
		int i, j;

		boolean continueLoop;
		int skip = 0;
		do {
			continueLoop = false;
			i = source.indexOf("&", skip);
			if (i > -1) {
				j = source.indexOf(";", i);
				if (j > i) {
					String entityToLookFor = source.substring(i + 1, j);
					//Logger.e("key=" + entityToLookFor);
					Integer value = htmlEntities.get(entityToLookFor);
					if (value != null && value > 0 && value < 65535) {
						source = source.substring(0, i)
								+ ((char) ((int) value))
								+ source.substring(j + 1);
						continueLoop = true;
					} else {
						skip = i + 1;
						continueLoop = true;
					}
				}
			}
		} while (continueLoop);
		return source;
	}
	
	public static String html2Text(String html) {
		StringBuffer filterText = new StringBuffer();

		Pattern p = Pattern.compile("<([^>]*)>");
		Matcher m = p.matcher(html);
		while (m.find()) {			
			m.appendReplacement(filterText, "");		
		}
		m.appendTail(filterText);
		
		return unescapeHTML(filterText.toString());
	}
	
	public static String trimHTMLTag(String text) {
		Pattern p = null;
		
		p = Pattern.compile("(\\r)+");
		text = p.matcher(text).replaceAll("\n");

		p = Pattern.compile("(\\n)+");
		text = p.matcher(text).replaceAll("\n");

		p = Pattern.compile("(^\\n+|\\n+$)");
		text = p.matcher(text).replaceAll("");
		
		StringBuffer filterText = new StringBuffer();

		p = Pattern.compile("<([^>]*)>");
		Matcher m = p.matcher(text);
		while (m.find()) {			
			if (m.group(1).equals("br /")) {
				m.appendReplacement(filterText, "\n");
			} else {
				m.appendReplacement(filterText, "");
			}			
		}
		m.appendTail(filterText);
		
		return filterText.toString();
	}
	
	/**
	 * 生成MD5字符串
	 * 
	 * @param text
	 * @return
	 */
	public static String md5(String text) {
		if (text == null) {
			return "";
		}

		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());

			byte[] digest = md.digest();

			for (int i = 0; i < digest.length; i++) {
				text = Integer.toHexString(0xFF & digest[i]);
				if (text.length() < 2) {
					text = "0" + text;
				}
				hexString.append(text);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hexString.toString();
	} 
	
	/**
	 * 设置软键盘的显示与隐藏
	 * @param content 可以触发软键盘的View
	 * @param visible
	 * @param delay 延迟时间
	 */
	public static void setSoftInputVisible(final View content, boolean visible, int delay) {
		 final InputMethodManager imm = (InputMethodManager)
				 content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		 
		 if(visible) {
			 if (delay > 0) {
				new Handler().postDelayed(new Runnable() {
				
					@Override
					public void run() {
						imm.showSoftInput(content, 0);
					}
				}, delay);
			 } else {
				 imm.showSoftInput(content, 0);  				
			 }
			 
		 } else {
			 if (delay > 0) {
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						imm.hideSoftInputFromWindow(content.getWindowToken(), 0);   
					}
				}, delay); 
			 } else {
				 imm.hideSoftInputFromWindow(content.getWindowToken(), 0);   
			 }
			  
		 }
	}
	
	public static class SizeComparator implements Comparator<Size> {

		@Override
		public int compare(Size s1, Size s2) {
			return s1.width > s2.width ? 1 : -1;
		}
		
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
	
	/**
	 * 通过InputStream对图片进行压缩
	 * @param stream1
	 * @param stream2
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromInputStream(InputStream stream1,
			InputStream stream2,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(stream1, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(stream2, null, options);
	}
	
	/**
	 * 通过文件对图片进行压缩
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampleBitmapFromFile(String path, 
			int reqWidth, int reqHeight) {
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
		
	}
	
	/**
	 * 将bitmap转化成byte数组
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static String dateFormat(long stamp) {
		Date date = new Date(stamp * 1000);
		Date today = new Date();

		SimpleDateFormat format;
		if (date.getDate() == today.getDate()) {
			format = new SimpleDateFormat("HH:mm");
		} else if (date.getYear() == today.getYear()) {
			format = new SimpleDateFormat("MM-dd HH:mm");
		} else {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}

		return format.format(date);
	}
	
}
