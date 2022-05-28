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
