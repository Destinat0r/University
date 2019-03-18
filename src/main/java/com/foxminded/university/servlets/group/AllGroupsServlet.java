package com.foxminded.university.servlets.group;

import java.io.IOException;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.foxminded.university.dao.impl.GroupDao;
import com.foxminded.university.domain.Group;

@WebServlet("/groups")
public class AllGroupsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Group> groups = new GroupDao().findAll();

        request.setAttribute("groups", groups);
        request.getRequestDispatcher("jsp/group/all_groups.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        
        new GroupDao().create(new Group(1, name));
        response.sendRedirect(request.getContextPath() + "/groups");
    }
}
