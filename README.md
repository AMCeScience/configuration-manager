configuration-manager
=====================

This is the configuration manager library for AMC eScience portals

configuration file
==================

The configuration file should look like the following example (i.e.: valid json format).

```json
{
    "item_1": "item",
    "item_2": [1, 2, 3],
    "item_3": {
        "item_1": "item"
    }
}
```

calls
=====

Instantiate the class and read/write to the specified keys.
Keys have to be passed in as they appear in the configuration json file.
Both the read and write methods use java's varargs, thus it is possible to input zero, one, or multiple items.
A call to either the read or write method with zero keys passed in will throw an error, as there can be no output when no keys are specified.

```java
ConfigurationManager manager = new ConfigurationManager();

// Read
// Pass in keys from json file in order
manager.read.getItem("item");
manager.read.getItem("item_1", "this_item");
manager.read.getItem(new String[]{"item_1", "this_item"});
// Etc.

// Write
manager.writer.write("test", "item_1", "this_item");
manager.writer.write("test", new String[]{"item_1", "this_item"});

// Error
manager.read.getItem();
manager.writer.write();
```
