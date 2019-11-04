package ru.pahaya.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.pahaya.servlets.AccountServlet.ID;

public class AccountServletValidator {

    public static void validate(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter(ID) == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
