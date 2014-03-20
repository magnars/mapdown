(ns mapdown.core-test
  (:require [mapdown.core :refer :all]
            [midje.sweet :refer :all]))

(fact
 (parse ":title abc") => {:title "abc"}
 (parse "
:title abc
:desc def") => {:title "abc", :desc "def"})

(fact
 (parse "
:section1
abc
:section2
def
ghi") => {:section1 "abc", :section2 "def\nghi"})

(fact
 "Empty sections are excluded."

 (parse "
:title abc

:section1

def

:illustration ghi

:section2") => {:title "abc", :section1 "def", :illustration "ghi"})

(fact
 "To avoid nasty surprises, we don't tolerate text prior the first
 key. It would have nowhere to go, becoming quite literally trailing
 trash."

 (parse "trash
:title abc") => (throws Exception "Mapdown content must start with a key - or the content has nowhere to go."))
