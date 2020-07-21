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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Task;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private List<String> funfacts;

    @Override
    public void init() {
        funfacts = new ArrayList<>();
        funfacts.add("PCs went by the name 'Electronic Brains' in the 1950s.");
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the input from the form.
        String text = getParameter(request, "text-input", "");
        String expression = getParameter(request, "cal", "null");

        
        boolean addition = expression.equals("add") ? true : false;
        boolean multiply = expression.equals("mul") ? true : false;

        // Break the text into individual input.
        String[] input = text.split("\\s*,\\s*");
        int[] inputs = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            inputs[i] = Integer.valueOf(input[i]);
        }

        int result = inputs[0]; 

        for (int i = 1; i <inputs.length; i++) {
            int num = inputs[i];
            // Add all input
            if (addition) {
                result += num;
            }
            // Multiply all input
            else if (multiply) {
                result *= num;
            }
            // nothing selected 
            else {
                result = 0;
                break;
            }
        }

        // Store in Datastore
        long timestamp = System.currentTimeMillis();

        Entity taskEntity = new Entity("Task");
        taskEntity.setProperty("inputs", text);
        taskEntity.setProperty("total", result);
        taskEntity.setProperty("timestamp", timestamp);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(taskEntity);

        // loading entities
        Query query = new Query("Task").addSort("timestamp", SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);

        List<Task> tasks = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            long id = entity.getKey().getId();
            long total = (long) entity.getProperty("total");
            long curr_stamp = (long) entity.getProperty("timestamp");
            Task task = new Task(id, total, curr_stamp);
            tasks.add(task);
        }

        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println("Entities: ");
        response.getWriter().println(gson.toJson(tasks));

        // Respond with the result.
        response.setContentType("text/html;");
        response.getWriter().println("Current Total = " + result);
    }

    /**
    * @return the request parameter, or the default value if the parameter
    *         was not specified by the client
    */
    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
        return defaultValue;
        }
        return value;
    }

}
