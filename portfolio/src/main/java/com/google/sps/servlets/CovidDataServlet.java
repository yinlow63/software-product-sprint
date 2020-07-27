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

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns covid data as a JSON object, e.g. {"1": 52, "2": 34}] */
@WebServlet("/covid-data")
public class CovidDataServlet extends HttpServlet {

  private LinkedHashMap<Integer, Integer> covidsCases = new LinkedHashMap<>();

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/us.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      Integer year = Integer.valueOf(cells[0]);
      Integer sightings = Integer.valueOf(cells[1]);

      covidsCases.put(year, sightings);
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(covidsCases);
    response.getWriter().println(json);
  }
}
