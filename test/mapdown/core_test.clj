(ns mapdown.core-test
  (:require [mapdown.core :refer :all]
            [midje.sweet :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]))

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
 "80 dashes means we're creating a list of maps."

 (parse "
--------------------------------------------------------------------------------
:title First item
:body

Some text
--------------------------------------------------------------------------------
:title Second item
:body

More text
--------------------------------------------------------------------------------
") => [{:title "First item", :body "Some text"}
       {:title "Second item", :body "More text"}])

(fact
 "To avoid nasty surprises, we don't tolerate text prior the first
 key. It would have nowhere to go, becoming quite literally trailing
 trash."

 (parse "trash
:title abc") => (throws Exception "Mapdown content must start with a key - or the content has nowhere to go."))

(fact
 "To help find errors, report the list location."

 (parse "
--------------------------------------------------------------------------------
:title abc
--------------------------------------------------------------------------------
trash
:title def
--------------------------------------------------------------------------------
") => (throws Exception "Error in 2nd entry: Mapdown content must start with a key - or the content has nowhere to go."))

(fact
 "You can parse files, just to get proper error messages."

 (with-files [["/file.md" ":title abc"]]
   (parse-file (str tmp-dir "/file.md")) => {:title "abc"})

 (with-files [["/file.md" "bleh"]]
   (parse-file (str tmp-dir "/file.md"))
   => (throws Exception (str "Error when parsing '" tmp-dir "/file.md': Mapdown content must start with a key - or the content has nowhere to go."))))

(fact
 "You can slurp entire directories in, finding files based on a regexp."

 (with-files [["/file.md" ":title abc"]
              ["/more/stuff.md" ":title def"]]
   (slurp-directory tmp-dir #"\.md$")
   => {"/file.md" {:title "abc"}
       "/more/stuff.md" {:title "def"}})

 (with-files [["/file.md" "bleh"]]
   (slurp-directory tmp-dir #"\.md$")
   => (throws Exception (str "Error when parsing '" tmp-dir "/file.md': Mapdown content must start with a key - or the content has nowhere to go."))))
