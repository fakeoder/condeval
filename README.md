# condeval

###1.description  
an expression eval tool

###2.supported operators


|symbol|usage|args|description|  
|:----:|:----:|:----:|:----:|
|+|a + b|a,b(two numbers)|calculate sum|    
|-|a - b|a,b(two numbers)|calculate minus|
|*|a * b|a,b(two numbers)|calculate time|
|/|a / b|a,b(two numbers)|calculate div|
|sqrt|sqrt(a)|a(one number)|calculate sqrt|
|(|(|delimiter no args|define calculate priority|
|)|)|delimiter no args|define calculate priority|
|&&|a && b|a,b(two boolean values)|and operator|
|&#124;&#124;|a&#124;&#124;b|a,b(two boolean values)|or operator|
|&gt;|a > b|a,b(two numbers)|return a > b|
|<|a < b|a,b(two numbers)|return a < b|
|>=|a >= b|a,b(two numbers)|return a >= b|
|<=|a <= b|a,b(two numbers)|return a <= b|
|==|a == b|a,b(two numbers)|return a == b|
|indexOf|indexOf(a,b)|a,b(two Strings)|return index of b in a|
|contains|contains(a,b)|a,b(two Strings)|return if a contains b|
|startWith|startWith(a,b)|a,b(two Strings)|return if a startWith b|
|endWith|endWith(a,b)|a,b(two Strings)|return if a endWith b|
|subString|subString(a,b,c)|a(String),b,c(two Integers)|return substring from b to c in a|
|concat|concat(a,b)|a,b(two Strings)|return combine a and b|
|equals|equals(a,b)|a,b(two Strings)|return string a equals b|

