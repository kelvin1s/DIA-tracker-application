# DIA Live Tracker

## Description
DIA Live Tracker is a Java application that retrieves the latest available price of the SPDR Dow Jones Industrial Average ETF Trust (DIA) using the Twelve Data financial market API in real-time.

The application utilises REST API to send HTTP GET requests at 15-second intervals in order to retrieve the DIA value extracted from the API response. The timestamp of the value is also collected. Both of these values are them stored in a queue data structure via a java linkedlist. 

The collected data is then used to plot a matplotlib graph to visually represent how the DIA values changes over time. The amount of collected data points can be altered by editing the for loop to iterate more of less times.

I created this app as part of the tasks set in Citi's Technology Software Development on Forage!

## Prerequisites
- A Google account to utilise Google Colab
- A Twelve Data account to access the Twelve Data API key

## Setup
1. Sign up for a Twelve Data account to obtain an API key: https://twelvedata.com/
2. Configure your API key as an environment variable in the Colab notebook.
3. Run each code cell to collect the price and timestamp market data, and produce the graph visualisation
