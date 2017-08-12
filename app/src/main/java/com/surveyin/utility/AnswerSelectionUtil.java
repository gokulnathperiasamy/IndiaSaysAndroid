package com.surveyin.utility;

import com.surveyin.entity.QuestionOptions;

public abstract class AnswerSelectionUtil {

    public static String getFormattedQuestionID(QuestionOptions questionOptions, String gender, String option) {
        return TextUtil.removeSpaces(TextUtil.trimStringLength(questionOptions.question, 200)
                + TextUtil.STRING_DELIMITER
                + gender
                + TextUtil.STRING_DELIMITER
                + option);

    }

}
