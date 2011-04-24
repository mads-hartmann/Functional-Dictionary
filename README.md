# Functional Dictionary - A Lift based application

This is a [Lift](http://www.liftweb.net "Lift") based web application. It's a dictionary of Functional Programming terms. The purpose of this app is to show people a working Lift based application with a simple purpose. The soruce code is full of links to the chapters in [Simply Lift](http://simply.liftweb.net/ "Simply Lift") that describe how specific things work in detail. So if you're new to Lift and want to get a general idea of what a Lift apllication looks like then take a look around.

Another great example of a fully functional Lift application is the [The ubiquitous chat app](http://simply.liftweb.net/index-Chapter-2.html#toc-Chapter-2 "The ubiquitous chat app").

To check out a live version of the master branch [here](http://functionaldictionary.mads379.staxapps.net/ "here").
For more information about Lift look [here](http://www.liftweb.net/ "here).

## Info

* [src/main/webapp](https://github.com/mads379/Functional-Dictionary/tree/master/src/main/webapp "src/main/webapp") contains the templates
* [src/main/scala/com/sidewayscoding](https://github.com/mads379/Functional-Dictionary/tree/master/src/main/scala/com/sidewayscoding "src/main/scala/com/sidewayscoding") contains the Scala code

Here are some classes you might want to look at:

* [Boot](https://github.com/mads379/Functional-Dictionary/blob/master/src/main/scala/bootstrap/liftweb/Boot.scala "Boot") is the class that Lift runs upon booting your application. It defines the menu structure and other configuration related stuff.
* [EntryServer](https://github.com/mads379/Functional-Dictionary/blob/master/src/main/scala/com/sidewayscoding/comet/EntryServer.scala "EntryServer) is the class which stores all of the entries in the dictionary. It simply stores the entries in the RAM to simplify the example.
* [EntryComet](https://github.com/mads379/Functional-Dictionary/blob/master/src/main/scala/com/sidewayscoding/comet/EntryComet.scala "EntryComet") is the class that displays a single entry. It's a CometActor. If another user adds a new description to the entry or votes on any of the existing ones it will update dynamically.
* [EntrySnippet](https://github.com/mads379/Functional-Dictionary/blob/master/src/main/scala/com/sidewayscoding/snippet/EntrySnippet.scala "EntrySnippet") is the class that displays and processes the form.

## How to run it

You need to have [SBT](http://code.google.com/p/simple-build-tool/ "SBT") 0.7.x installed. Then simply run

<pre><code>
> git clone git://github.com/mads379/Functional-Dictionary.git functional-dictionary
> cd functional-dictionary
> sbt
> update
> jetty-run
</code></pre>

