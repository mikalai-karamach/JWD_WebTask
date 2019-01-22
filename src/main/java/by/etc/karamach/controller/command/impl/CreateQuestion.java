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

public class CreateQuestion implements Command {
    private static final QuestionService questionService = ServiceFactory.getInstance().getQuestionService();

    private static final String TEST_PAGE_URL = "controller?command=edit_test&test_id=";
    private static final transient Logger logger = LogManager.getLogger();

    @Override
    public void executeTask(HttpServletRequest req, HttpServletResponse resp) throws CommandException {

        HttpSession existingSession = SessionHelper.getExistingSession(req);
        int userId = (int) existingSession.getAttribute(SessionAttributeName.ID);

        Optional<Integer> testId = RequestDataExecutor.getIntegerByName(RequestParameterName.TEST_ID, req);

        Optional<String> description = RequestDataExecutor.getStringByName(RequestParameterName.DESCRIPTION, req);

        try {

            if ((!testId.isPresent()) || (!description.isPresent())) {

                DispatchAssistant.redirectToJsp(req, resp, JspPageName.INVALID_REQUEST_PARAMETER);

            } else {

                questionService.createQuestion(testId.get(), description.get(), userId);

                resp.sendRedirect(TEST_PAGE_URL + testId.get());
            }

        } catch (ServiceException e) {

            throw new CommandException(e);

        } catch (IOException | ServletException e) {

            logger.error(e);
            throw new RuntimeException(e);

        }
    }
}
