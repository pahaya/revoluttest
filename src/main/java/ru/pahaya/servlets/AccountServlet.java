package ru.pahaya.servlets;

import com.google.gson.Gson;
import ru.pahaya.dao.Account;
import ru.pahaya.services.AccountService;
import ru.pahaya.services.SimpleAccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccountServlet extends HttpServlet {

    private static final AccountService ACCOUNT_SERVICE = new SimpleAccountService();
    private static final AccountServletValidator ACCOUNT_SERVLET_VALIDATOR = new AccountServletValidator();
    private static final Gson gson = new Gson();
    static final String ID = "id";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountServletValidator.validate(req, resp);
        String id;
        if ((id = req.getParameter(ID)) != null) {
            Account account = ACCOUNT_SERVICE.get(id).get();
            resp.getOutputStream().println(gson.toJson(account));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
