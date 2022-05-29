Cách sao chép dữ liệu từ Assets vào hệ thống mobile

Step 1: 

    String DATABASE_NAME = "dbContact.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

Step 2: lấy đường dẫn lưu trữ

    private String layDuongDanLuuTru(){
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

Step 3: 

//private app
    
    File dbFile = getDatabasePath(DATABASE_NAME);

    if (!dbFile.exists())
    {
        try
        {
            CopyDataBaseFromAsset();
            Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

Step 4: 
    
    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = layDuongDanLuuTru();
            
            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
            
        } catch (Exception ex) {
            Log.e("LOI_SAO_CHEP", ex.toString());
        }
    }


TƯƠNG TÁC VỚI SQLite

* Mở CSDL trước:

      private void xuLyCSDL() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
		
    //Truy vấn lấy dữ liệu ra 
    //cursor ban đầu trỏ tới null -> dùng moveNextTo() để trỏ tới dòng kế tiếp
    //-> khi hết dòng thì moveNextTo() sẽ bằng false

          Cursor cursor = database.query("Contact", null, null, null, null, null, null);
          dsDanhBa.clear();
          while (cursor.moveToNext()) {
              int ma = cursor.getInt(0);
              String ten = cursor.getString(1);
              String phone = cursor.getString(2);
              dsDanhBa.add(new Contact(String.valueOf(ma), ten, phone));
          }
          cursor.close();
          contactAdapter.notifyDataSetChanged();
    }




* Thêm dữ liệu
* 
    private void xyLyThemDanhBa(String hoTen, String phone) {
        ContentValues row = new ContentValues();
        int ma = Integer.parseInt(dsDanhBa.get(dsDanhBa.size() - 1).getMa() + 1);

        row.put("Ma", ma);
        row.put("Ten", hoTen);
        row.put("Phone", phone);
        long r = database.insert("Contact", null, row);
        Toast.makeText(this, "Thêm danh bạ thành công", Toast.LENGTH_SHORT).show();
        hienThiDanhSachDanhBa();
  }

    ContentValues row = new ContentValues();
    row.put("Tên cột", values);
    row.put("Tên cột", values);
    ...
    long r = database.insert("tên bảng",null,row);
	
    Toast.makeText(Context, "Vừa thêm mới 1 contact, kết quả trạng thái =" + r,Toast.LENGTH_LONG).show();
	
* Cập nhật dữ liệu

  private void xyLySuaThongTin(String ma, String ten, String phone) {
  ContentValues row = new ContentValues();

        row.put("Ten", ten);
        row.put("Phone", phone);
        database.update("Contact", row, "ma=?", new String[]{ma});
        hienThiDanhSachDanhBa();
  }
    ContentValues row = new ContentValues();
    row.put("Cột cần sửa", new values);
    database.update("tên bảng", row, "(điều kiện) ma=?", new String[]{"3"});


* Xoá dữ liệu

    database.delete("Contact", "Ma=?", new String[]{ma});

    -Xoá toàn bộ dữ liệu
        database.delete("tên bảng", null, null);
    -Xoá theo điều kiện
        String malop = "1"
        database.delete("tên bảng", malop=?, new String[] {malop};);

  * Xử lý event

    private void xyLyNhanTin() {
    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
    smsIntent.setType("vnd.android-dir/mms-sms");
    smsIntent.putExtra("address", txtSoDienThoai.getText().toString());
    smsIntent.putExtra("sms_body", "");
    startActivity(smsIntent);
    }

    private void xyLyCall() {
    Intent intent = new Intent(Intent.ACTION_CALL);
    Uri uri = Uri.parse("tel:" + txtSoDienThoai.getText().toString());
    intent.setData(uri);
    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(ChiTietDanhBaActivity.this, new String[] {Manifest.permission.CALL_PHONE},1);
    }else{
    startActivity(intent);
    }
    }
	

