package cn.edu.ldu.sniffer.filter;

import java.util.Arrays;
import java.util.List;

//过滤器逻辑字符串解析类
public class Filters {
    private final static List<String> FILTER_WORDS = Arrays.asList("and", "or");
    private Filters() {
    }
    //过滤器字符串解析
    public static Filter parseFilter(String filterString) throws InvalidFilterException {
        if (filterString.isEmpty()) {
            throw new IllegalArgumentException("过滤字符串不能为空！");
        }
        filterString = filterString.toLowerCase().trim();
        boolean leftSideNegate = false;
        Filter leftSide = null;
        if (filterString.startsWith("not")) {
            leftSideNegate = true;
            filterString = filterString.replaceFirst("not", "").trim();
        }
        //以左括号开始
        if (filterString.startsWith("(")) {
            String clause = getClauseBetweenParenthesis(filterString);
            leftSide = parseFilter(clause); //解析括号里的内容
            if (leftSideNegate) {
                leftSide = new NotFilter(leftSide);
            }
            int rightSideStartIndex = filterString.indexOf(clause) + clause.length() + 2; // + 2 跳过左右括号
            if (rightSideStartIndex <= filterString.length() - 1) {
                filterString = filterString.substring(rightSideStartIndex);
            } else { //解析结束
                return leftSide;
            }
        }
        String[] parts = filterString.split(" ");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (FILTER_WORDS.contains(part)) {
                if (leftSide == null) {
                    leftSide = parseFilter(joinParts(parts, 0, i));
                    if (leftSideNegate) {
                        leftSide = new NotFilter(leftSide);
                    }
                }
                Filter rightSide = parseFilter(joinParts(parts, i + 1, parts.length));
                switch (part) {
                    case "and":
                        return new AndFilter(leftSide, rightSide);
                    case "or":
                        return new OrFilter(leftSide, rightSide);
                }
            }
        }
        return leftSideNegate ? new NotFilter(new FieldFilter(filterString)) : new FieldFilter(filterString);
    }
    private static String joinParts(String[] parts, int startIndex, int endIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            sb.append(parts[i]).append(" ");
        }
        return sb.toString().trim();
    }
    static String getClauseBetweenParenthesis(String string) throws InvalidFilterException {
        if (string.isEmpty()) {
            throw new IllegalArgumentException("被解析字符串为空！");
        }
        if (string.charAt(0) != '(') {
            throw new IllegalArgumentException("字符串不以'('开始");
        }
        int openParenthesis = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ')') {
                openParenthesis--;
                if (openParenthesis == 0) {
                    return string.substring(1, i);
                }
            }
            if (string.charAt(i) == '(') {
                openParenthesis++;
            }
        }
        throw new InvalidFilterException("括号内字符串解析出现错误！");
    }
}
