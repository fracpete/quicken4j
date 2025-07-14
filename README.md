# quicken4j
Java library for reading Quicken Exchange Format (QIF) files.

## File format
See description of QIF file format on WikiPedia:

https://en.wikipedia.org/wiki/Quicken_Interchange_Format

## Maven
Use the following dependency to include it in your Maven project:

```xml
    <dependency>
      <groupId>com.arepresas</groupId>
      <artifactId>quicken4j</artifactId>
      <version>0.0.1</version>
    </dependency>
```

## Example
The following example reads in the file `simple.qif` and prints out for 
each transaction the date, amount and payee.

```java
import java.io.File;

import com.arepresas.quicken4j.QIFReader;
import com.arepresas.quicken4j.Transaction;
import com.arepresas.quicken4j.Transactions;
...
QIFReader reader = new QIFReader();
Transactions trans = reader.read(new File("simple.qif"));
for(
Transaction t:trans)
  System.out.

println(t.getDate() +"\n  "+t.

getAmount() +"\n  "+t.

getPayee());
```

You can also read them with a specific encoding, e.g., `Windows-1252`:

```java
import java.io.File;
import java.nio.charset.Charset;

import com.arepresas.quicken4j.QIFReader;
import com.arepresas.quicken4j.Transaction;
import com.arepresas.quicken4j.Transactions;
...
QIFReader reader = new QIFReader();
Transactions trans = reader.read(new File("simple.qif", Charset.forName("Windows-1252")));
for(
Transaction t:trans)
  System.out.

println(t.getDate() +"\n  "+t.

getAmount() +"\n  "+t.

getPayee());
```
