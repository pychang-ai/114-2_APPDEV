# Week14 exercise
## Error01
change
```
 rintent.putExtras(bundle);
```
to
```
 rintent.putExtras(rbundle);
```

## Error02
change
```
if (requestCode == RESULT_OK) {
```
to
```
if (requestCode == SET_RESULT && resultCode == RESULT_OK) {
```
