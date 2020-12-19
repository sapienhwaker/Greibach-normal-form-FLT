# Formal Language Theory
> Java implementation of GNF.

[![Generic badge](https://img.shields.io/badge/Written%20in-Java-green.svg)](https://shields.io/)

Greibach Normal Form (GNF) A CFG G = (V,T,R,S) is said to be in GNF if every production is of the form A → aα, where a ∈ T and α ∈ V ∗, i.e., α is a string of zero or more variables. Definition: A production U ∈ R is said to be in the form left recursion, if U : A → Aα for some A ∈ V .

## Objectives:
* Read Input from the file
* Remove unproductive symbols
* Remove unreachable symbols
* Remove null productions
* Remove unit productions
* Eliminate the immediate left recursion
* Write output to the file

## Input
Text file contining the non-simplied grammar.
All string enclosed with <> are the non-terminal symbols. Rest all character/s are the terminals.

For examaple:
\<expression> ::= \<expression> + \<term>
\<expression> ::= \<term>
\<term> ::= \<term> * \<factor>
\<term> ::= \<factor>
\<factor> ::= ( \<expression> )
\<factor> ::= id


## Output
Text file with simplified grammar.
Similified form of the above example:
\<expression1> ::= +\<term> 
\<expression1> ::= +\<term> \<expression1> 
\<expression> ::= \<term> 
\<expression> ::= \<term> \<expression1> 
\<factor> ::= ( \<expression> ) 
\<factor> ::= id 
\<term1> ::= *\<factor> 
\<term1> ::= *\<factor> \<term1> 
\<term> ::= \<factor> 
\<term> ::= \<factor> \<term1> 

## Advantages of GNF

* 1 Every derivation of a string scontains |s| rule applications.
* 2 Greibach normal form grammars can easily be converted to pushdown automata with no -transitions.  This is useful because such PDAs are   guaranteed to halt.

## Release History

* 0.1.1
    * CHANGE: Update docs (module code remains unchanged)
* 0.1.0
    * The first proper release
* 0.0.1
    * Work in progress

## Meta

Author: [Prasad Hajare](https://www.itsprasad.com/), MS Computer Science.

Distributed under the [MIT License](LICENSE).


<!-- Markdown link & img dfn's -->
[npm-image]: https://img.shields.io/npm/v/datadog-metrics.svg?style=flat-square
[npm-url]: https://npmjs.org/package/datadog-metrics
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/yourname/yourproject/wiki
