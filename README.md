# Customer processing

## Stack
Java, preferably with springboot

## Requires
- Supported formats CSV, XML
- Parse and validate records
- Import new records

## Output
- All transactions need to be Unique
- End balance needs to validated
- A draft of each transaction with the transaction reference and description of any failed record

## Functions
Upload supports; 
- basic CSV( with comma delimiter, positition of columns can be scrambled)
- XML

Example usage:
```
http://localhost:8080/customers/upload
[from-data (POST)]
{
	file: "...file.xml"
}
```