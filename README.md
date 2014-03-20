# Mapdown [![Build Status](https://secure.travis-ci.org/magnars/mapdown.png)](http://travis-ci.org/magnars/mapdown)

A lightweight markup format to turn strings into maps.

Why? Well, sometimes you want metadata with your markup format of
choice. Let's say you're writing some markdown articles, and want to
add information about the author and date.

- Do you write .edn files, putting your markdown inside multiline
  strings with the escape nightmares that entails?

- Or do you put the metadata in another file to write markdown in its
  natural environment?

With Mapdown you add the metadata alongside your content, while
avoiding string escaping and regaining editor support.

## Install

Add `[mapdown "0.1.0"]` to `:dependencies` in your `project.clj`.

Please note that this project uses
[Semantic Versioning](http://semver.org/). As long as we're on a `0`
major version, there might be API changes. Pay attention when
upgrading to a new minor version. As soon as we're on a `1` major
version, there will be no breaking changes without a major version
increase.

## Usage

With `(mapdown/parse str)` you turn this:

```txt
:title Mapdown example
:author Magnar Sveen
:body

Here's an example of how mapdown works.

It's, like, text with keywords.

:aside

There's not much to it, really.
```

into this:

```clj
{:title "Mapdown example"
 :author "Magnar Sveen"
 :body "Here's an example of how mapdown works.\n\nIt's, like, text with keywords."
 :aside "There's not much to it, really."}
```

### Supplementary features

You can also parse the contents of a file with `(parse-file "path")`.
Why not just slurp it in yourself? Just to get error messages that
include the file path.

## Contribute

Yes, please do. And add tests for your feature or fix, or I'll
certainly break it later.

#### Running the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.

## License

Copyright © 2013-2014 Magnar Sveen

Distributed under the Eclipse Public License, the same as Clojure.
