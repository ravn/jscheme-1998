# JScheme 1998

**IMPORTANT:  Java 8 required for compilation**

Experimental mavenization of JScheme 1998. 

Expected usage is as a configuration-with-code file parser
replacing property files. 

Licensing is the same as the original - see original-license.html

Note:  This is a repackaging of the original from <http://norvig.com/jscheme.html>
and not the later larger version found on <http://jscheme.sourceforge.net/jscheme/main.html>


Note:  Can be executed with "java -jar ..." to go in REPL.

/ravn 2012-01-13

Additional tests added preparing for embedded use.

JSR-223 support will most likely be the easiest way to do this.

/ravn 2012-06-27

Updated to latest dependencies to silence dependabot.  Due to the deprecation
of older Java versions in `javac` (see <https://openjdk.org/jeps/182>) the sources
require Java 8 or 11 to keep targeting Java 6.  This is being enforced in
the POM.  The Github Actions scripts use Java 11.

And my experiments back then with scripting using embedded JScheme unfortunately
turned out to be too complex for our needs.  
The configuration bit were done using 
dependency injection (using Dagger2 due to reflection being very slow on the 
AS/400 Classic JVM) along with properties.   I see why having configuration
code being executed at actual runtime has benefits, but having a full 
language makes it too tempting to do more than absolutely necessary.  
Properties - perhaps combined with a very simple IF-THEN-ELSE mechanism - solves 
most of the problems I've actually seen in the wild.  You can typically
always go back and put the needed logic in the application itself.

Also were I to repeat the exercise today I 
would have a very close look at having the main code with configuration in a source
file which was then run as described in <https://openjdk.org/jeps/330> (Java 11+).

/ravn 2022-10-01