// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private List<String> funfacts;

    @Override
    public void init() {
        funfacts = new ArrayList<>();
        funfacts.add("PCs went by the name “Electronic Brains” in the 1950s.");
        funfacts.add("Email has been around longer than the World Wide Web.");
        funfacts.add("The first computer mouse was invented by Doug Engelbart and it was carved from wood.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fact = funfacts.get((int) (Math.random() * funfacts.size()));
        response.setContentType("text/html;");

        // String greetings = "How are you?";
        // response.getWriter().println(greetings);
    
        response.getWriter().println(fact);
    }
}
