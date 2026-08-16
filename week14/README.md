# Week14 exercise
## Error01
- 檔案：MainActivity.java	
- 行數：53	
- 問題：if (requestCode == RESULT_OK) — 永遠為 false，因為 requestCode = 1 而 RESULT_OK = -1	
- 修正：改為 if (resultCode == RESULT_OK && data != null)，並加上 bundle null 檢查
## Error02
- 檔案：OpActivity.java	
- 行數：70	
- 問題：rintent.putExtras(bundle) — 把輸入的 operand 資料傳回去，而不是計算結果	
- 修正：改為 rintent.putExtras(rbundle)，正確傳回 RESULT
