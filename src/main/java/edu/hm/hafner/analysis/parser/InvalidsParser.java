package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for Oracle Invalids.
 *
 * @author Ullrich Hafner
 */
public class InvalidsParser extends RegexpLineParser {
    private static final long serialVersionUID = 440910718005095427L;
    static final String WARNING_PREFIX = "Oracle ";
    private static final String INVALIDS_PATTERN = "^\\s*(\\w+),([a-zA-Z#_0-9/]*),([A-Z_ ]*),(.*),(\\d+),\\d+,([^:]*)"
            + ":\\s*(.*)$";

    /**
     * Creates a new instance of {@link InvalidsParser}.
     */
    public InvalidsParser() {
        super(INVALIDS_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String type = WARNING_PREFIX + StringUtils.capitalize(StringUtils.lowerCase(matcher.group(4)));
        String category = matcher.group(6);
        Severity priority;
        if (StringUtils.contains(category, "PLW-07")) {
            priority = Severity.WARNING_LOW;
        }
        else if (StringUtils.contains(category, "ORA")) {
            priority = Severity.WARNING_HIGH;
        }
        else {
            priority = Severity.WARNING_NORMAL;
        }
        return builder.setFileName(matcher.group(2) + "." + matcher.group(3))
                      .setLineStart(matcher.group(5)).setType(type).setCategory(category)
                      .setPackageName(matcher.group(1)).setMessage(matcher.group(7)).setSeverity(priority)
                      .build();
    }
}

