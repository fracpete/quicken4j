# quicken4j
Java library for reading Quicken Exchange Format (QIF) files.

## File format
See description of QIF file format on WikiPedia:

https://en.wikipedia.org/wiki/Quicken_Interchange_Format

## Maven
Use the following dependency to include it in your Maven project:

```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>quicken4j</artifactId>
      <version>0.0.2</version>
    </dependency>
```

## Example
The following example reads in the file `simple.qif` and prints out for 
each transaction the date, amount and payee.

```java
import java.io.File;
import com.github.fracpete.quicken4j.QIFReader;
import com.github.fracpete.quicken4j.Transaction;
import com.github.fracpete.quicken4j.Transactions;
...
QIFReader reader = new QIFReader();
Transactions trans = reader.read(new File("simple.qif"));
for (Transaction t: trans)
  System.out.println(t.getDate() + "\n  " + t.getAmount() + "\n  " + t.getPayee());
```
