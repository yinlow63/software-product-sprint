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


google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

// Hello World Edition
// /** Creates a chart and adds it to the page. */
// function drawChart() {
//   const data = new google.visualization.DataTable();
//   data.addColumn('string', 'Fruits');
//   data.addColumn('number', 'Count');
//         data.addRows([
//           ['Apple', 20],
//           ['Orange', 15],
//           ['Pear', 5]
//         ]);

//   const options = {
//     'title': 'Fruits',
//     'width':500,
//     'height':400
//   };

//   const chart = new google.visualization.PieChart(
//       document.getElementById('chart-container'));
//   chart.draw(data, options);
// }

/** Fetches covid data and uses it to create a chart. */
function drawChart() {
  fetch('/covid-data').then(response => response.json())
  .then((covidsCases) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Month');
    data.addColumn('number', 'Total Detected Case');
    Object.keys(covidsCases).forEach((year) => {
      data.addRow([year, covidsCases[year]]);
    });

    const options = {
      'title': 'COVID-19 US Monthly Data in 2020',
      'width':1000,
      'height':500
    };

    const chart = new google.visualization.LineChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
  });
}