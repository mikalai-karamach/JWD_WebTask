package by.etc.karamach.controller;

import by.etc.karamach.controller.command.Command;
import by.etc.karamach.controller.command.CommandException;
import by.etc.karamach.controller.command.CommandProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.etc.karamach.controller.RequestParameterName.ERROR_MSG;

public final class Controller extends HttpServlet {

    private static final String TEXT_HTML = "text/html";
    private static final String ERROR_TEXT = "E R R O R";
    private static final long serialVersionUID = 86004574040274668L;

    //TODO: QUESTION: Where shell we do it?

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter(RequestParameterName.COMMAND_NAME);
        Command command = CommandProvider.getInstance().getCommand(commandName);

        try {
            command.executeTask(req, resp);
        } catch (CommandException e) {
            //TODO: LOG !
            //TODO: Place errormsg at jsp
            req.setAttribute(ERROR_MSG, e.getMessage());
            redirectToErrorPage(req, resp);
        }
//        catch (Exception e) {
//            redirectToErrorPage(req, resp);
//        }
    }

    private void redirectToErrorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RequestDispatcher dispatcher = req.getRequestDispatcher(JspPageName.ERROR_PAGE);

        if (dispatcher != null) {
            dispatcher.forward(req, resp);
        } else {
            errorMessageDirectlyFromResponse(resp);
        }
    }

    private void errorMessageDirectlyFromResponse(HttpServletResponse resp) throws IOException {
        resp.setContentType(TEXT_HTML);
        resp.getWriter().println(ERROR_TEXT);
    }
}