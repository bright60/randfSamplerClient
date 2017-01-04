# R and F Java Sampler Client for JMeter
JMeter Java Sampler Client for Testing Mule Asynchronous Flows

This codebase provides an initial example of working with JMeter's `JavaSamplerClient` when testing asynchronous Mule HTTP requests.

This example expects the flow listener to be defined as an HTTP connector. For the purpose of this example, the destination of the flow has been defined as a MongoDB connector but any database or file can be substituted.

Using the `sampleBegin` and `sampleEnd` methods of JMeter's `SampleResult`, the initial time is captured prior to the HTTP request. After the request is dispatched, a poller waits a user-specified amount of time prior to checking MongoDB for the correlating message number and its timestamp. Please note that the timestamp inserted into MongoDB is defined once the flow has _completed_ and not when the poller picks up the result.

# Setup
TBD
