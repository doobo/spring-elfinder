package cn.yours.elfinder.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpUtil {

	public static String getAttachementFileName(String fileName, String userAgent) throws UnsupportedEncodingException {
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
			if (userAgent.contains("msie")) {
				return "filename=\"" + URLEncoder.encode(fileName, UTF_8.name()) + "\"";
			}
			if (userAgent.contains("opera")) {
				return "filename*=UTF-8''" + URLEncoder.encode(fileName, UTF_8.name());
			}
			if (userAgent.contains("safari")) {
				return "filename=\"" + new String(fileName.getBytes(UTF_8.name()), "ISO8859-1") + "\"";
			}
			if (userAgent.contains("mozilla")) {
				return "filename*=UTF-8''" + URLEncoder.encode(fileName, UTF_8.name());
			}
		}
		return "filename=\"" + URLEncoder.encode(fileName, "UTF8") + "\"";
	}
}
