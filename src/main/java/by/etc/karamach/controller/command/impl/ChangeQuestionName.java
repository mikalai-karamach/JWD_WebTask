package by.etc.karamach.controller.command.impl;

import by.etc.karamach.controller.command.Command;
import by.etc.karamach.controller.command.CommandException;
import by.etc.karamach.controller.util.*;
import by.etc.karamach.service.QuestionService;
import by.etc.karamach.service.ServiceException;
import by.etc.karamach.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class ChangeQuestionName implements Command {

    private static final QuestionService questionService = ServiceFactory.getInstance().getQuestionService();
    private static final String QUESTION_PAGE_URL = "controller?command=edit_question&question_id=";
    private static final transient Logger logger = LogManager.getLogger();

    @Override
    public void executeTask(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        HttpSession existingSession = SessionHelper.getExistingSession(req);
        int userId = (int) existingSession.getAttribute(SessionAttributeName.ID);

        Optional<Integer> questionId = RequestDataExecutor.getIntegerByName(RequestParameterName.QUESTION_ID, req);

        Optional<String> newName = RequestDataExecutor.getStringByName(RequestParameterName.NAME, req);

        try {

            if ((!questionId.isPresent()) || (!newName.isPresent())) {

                DispatchAssistant.redirectToJsp(req, resp, JspPageName.INVALID_REQUEST_PARAMETER);

            } else {

                questionService.updateQuestionName(questionId.get(), newName.get(), userId);

                resp.sendRedirect(QUESTION_PAGE_URL + questionId.get());
            }

        } catch (ServiceException e) {

            throw new CommandException(e);

        } catch (IOException | ServletException e) {

            logger.error(e);
            throw new RuntimeException(e);

        }
    }
}
