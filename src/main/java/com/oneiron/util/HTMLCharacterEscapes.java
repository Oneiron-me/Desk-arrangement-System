package com.oneiron.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

public class HTMLCharacterEscapes extends CharacterEscapes {

	private static final long serialVersionUID = -276382374854418641L;

	private final int[] asciiEscapes;

	@SuppressWarnings("unused")
	private final CharSequenceTranslator translator;

	public HTMLCharacterEscapes() {
    
        // 1. XSS 방지 처리할 특수 문자 지정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
        
        Map<CharSequence, CharSequence> map = new HashMap<>();
        
        map.put("(", "&#40;");
        map.put("}", "&#41;");
        map.put("#", "&#35;");
        map.put("\'", "&#39;");
        
        // 2. XSS 방지 처리 특수 문자 인코딩 값 지정
        translator = new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_ESCAPE),  // <, >, &, " 는 여기에 포함됨
            new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE),
            new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE),
            // 여기에서 커스터마이징 가능
            new LookupTranslator(EntityArrays.invert(map))
        );        
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

	@Override
    public SerializableString getEscapeSequence(int ch) {
        
		//여긴 커스텀한거 쓸수있음
		//return new SerializedString(translator.translate(Character.toString((char) ch)));
        
        // 참고 - 커스터마이징이 필요없다면 아래와 같이 Apache Commons Lang3에서 제공하는 메서드를 써도 된다.
        return new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString((char) ch)));
    }
}