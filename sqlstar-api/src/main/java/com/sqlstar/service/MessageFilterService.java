package com.sqlstar.service;

import com.sqlstar.model.FilteredWord;
import com.sqlstar.repository.FilteredWordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageFilterService {

    private final FilteredWordRepository filteredWordRepository;

    public MessageFilterService(FilteredWordRepository filteredWordRepository) {
        this.filteredWordRepository = filteredWordRepository;
    }

    public Result censor(String text) {
        if (text == null || text.isBlank()) {
            return new Result(text == null ? "" : text, 0);
        }

        List<String> terms = filteredWordRepository.findAll()
                .stream()
                .map(FilteredWord::getWord)
                .filter(s -> s != null && !s.isBlank())
                .map(String::trim)
                .distinct()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .toList();

        if (terms.isEmpty()) {
            return new Result(text, 0);
        }

        List<Pattern> patterns = new ArrayList<>(terms.size());
        for (String term : terms) {
            Pattern p = buildPatternForTerm(term);
            if (p != null) {
                patterns.add(p);
            }
        }

        String result = text;
        int totalReplacements = 0;

        for (Pattern p : patterns) {
            Matcher m = p.matcher(result);
            StringBuffer sb = new StringBuffer();
            boolean foundAny = false;
            while (m.find()) {
                foundAny = true;
                String match = m.group();
                String masked = maskLettersDigitsUnderscore(match);
                m.appendReplacement(sb, Matcher.quoteReplacement(masked));
                totalReplacements++;
            }
            m.appendTail(sb);
            if (foundAny) {
                result = sb.toString();
            }
        }

        return new Result(result, totalReplacements);
    }

    // Build a case-insensitive regex that matches the term.
    // - If it's a single "word" ([A-Za-z0-9_]+) we use word boundaries: \bterm\b
    // - Otherwise, treat whitespace flexibly by converting runs of whitespace to \\s+ and escaping other characters.
    private Pattern buildPatternForTerm(String term) {
        String trimmed = term.trim();
        if (trimmed.isEmpty()) return null;

        boolean isWordLike = trimmed.matches("^[\\p{Alnum}_]+$");

        String regex;
        if (isWordLike) {
            regex = "\\b" + Pattern.quote(trimmed) + "\\b";
        } else {
            // Split on whitespace and join parts with \\s+ so phrases with spaces match flexibly
            String[] parts = trimmed.split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                if (i > 0) sb.append("\\s+");
                sb.append(Pattern.quote(parts[i]));
            }
            regex = sb.toString();
        }

        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    // Replace only letters/digits/underscore with '*', keep whitespace and punctuation
    private String maskLettersDigitsUnderscore(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetterOrDigit(c) || c == '_') {
                sb.append('*');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public record Result(String amendedText, int replacements) {}
}
