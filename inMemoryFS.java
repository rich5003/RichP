import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
/*
 * Author: Rich P 
 * 
 * Create a program that will mimic a file system in a non-persistent way (when program is restarted, your file system is empty).. 
 * Your file system should be capable of performing the following tasks:
 *
 * Create a new folder - Takes a parameter of absolute folder path
 * Create a new file - Take a parameter of absolute file path
 * Add content to a file - Take 2 parameters: Content to append to a file; Absolute path to a file
 * Copy files - Takes 2 parameters: Absolute path to a source file; Absolute path to a destination file (NOTE: If destination file exists, it will be overwritten)
 * Display file contents - Takes absolute path to a file as an input; Prints out file contents as an output
 * List folder contents - Takes absolute path to a folder as an input; Prints out folder contents as an output
 * Search for a file by name - Takes name of a file to find; Prints out list of absolute paths to files with matching names
 * Search for a file by name - Takes 2 parameters: Absolute path to a starting folder and file name; Outputs list of absolute paths to files with matching names
 * (Optional) Copy folders - Takes 2 parameters: Absolute path to source folder, Absolute path to destination folder
 *
 * Your program should be capable of executing commands similar to this:

 * mkdir /someName
 * create /file1
 * create /someName/file1
 * write "Some text" /file1
 * cat /file1
 * cp /file1 /someName/file2
 * find file2 (Should return all found locations for file2)
 * find /someName file2
 * ls /someName
 * cp /someName /copyFolder
 * 
 * 
 * Input: Based of which case (Option) have been selected it input as filename, path along with filename.
 * 
 * 
 * 
 * */

public class inMemoryFS {
	static boolean ff=false;

	public static void createFolder(String path) throws Exception {
		Boolean result=false;

		File directory= new File(path);
		if (!directory.exists()) {
			result = directory.mkdir();

			if (result) {
				System.out.println("Folder is created successfully...");
			} else {
				System.out.println(" Unable to Create Folder");
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	public  static void createFile(String path) throws Exception {
		Boolean r = null;

		File file= new File(path);
		r = file.createNewFile();

		if(r) {
			System.out.println("New File Created Successfully... ");
		}
		else
			System.out.println("Unable to create File. File name already exists...");
	}

	//----------------------------------------------------------------------------------------------
	public static void createFolderWithFile(String path) throws Exception {

		File targetFile = new File(path);
		File parent = targetFile.getParentFile();
		if(!parent.exists() && !parent.mkdirs()){
			throw new IllegalStateException("Couldn't create dir: " + parent);
		}
		boolean r = targetFile.createNewFile();

		if(r) {
			System.out.println("New File Created Successfully... ");
		}
		else
			System.out.println("Unable to create File. File name already exists...");

	}

	//----------------------------------------------------------------------------------------------

	public static void appendToFile(String path, String contents)throws Exception {

		FileWriter fw = new FileWriter(path,true); 
		fw.write(contents);
		fw.write("\n");
		fw.close();
	}

	//-----------------------------------------------------------------------------------------------	
	static void displayFile(String path) throws Exception {
		String line=null;
		File fin = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(fin));
		while ((line=reader.readLine()) != null) 
			System.out.println(line);
		reader.close();
	}
	//---------------------------------------------------------------------------------------------------
	public static void copyFiles(String srcPath, String destPath)throws Exception {

		File targetFile = new File(destPath);
		File parent = targetFile.getParentFile();
		if(!parent.exists() && !parent.mkdirs()){
			//throw new IllegalStateException("Couldn't create dir: " + parent);
		}
		boolean r = targetFile.createNewFile();

		File fout = new File(destPath);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
		File fin = new File(srcPath);
		BufferedReader reader = new BufferedReader(new FileReader(fin));
		String line = null;
		while ((line=reader.readLine()) != null) {
			writer.write(line);
			writer.newLine();
		}
		reader.close();
		writer.close();
	}

	//--------------------------------------------------------------------------------------------------
	static void searchFilesinFullSystem(File root, String fName) throws Exception {
		try {
			boolean fileFound = false;

			if(root.isDirectory()) {
				File[] fi=root.listFiles();
				for(int i=0;i<fi.length;i++){
					if(fileFound==true) 
						break;
					searchFilesinFullSystem(fi[i], fName);
				}
			}
			else{
				if(root.getName().contains(fName)){     // if(root.getName().equals(fName)) gives the result conating the fName if exact file name is needed use equals
					System.out.println(root.getAbsolutePath());
					fileFound=true;

				}
			}
		}catch(Exception e) {

		}
	}

	//--------------------------------------------------------------------------------------------------
	static void searchFilesWithName(File root, List<File> lFile,String fName) throws Exception {
		if(root.isDirectory())
		{
			for(File file : root.listFiles())
				searchFilesWithName(file, lFile,fName);
		}
		else if(root.isFile() && root.getName().contains(fName))
		{
			lFile.add(root);
		}
	}

	//---------------------------------------------------------------------------------------------------
	static void listFolderContents(String path) {
		File f = new File(path);

		// Lists all files with its path
		//		ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));

		// Lists only file names
		ArrayList<String> files = new ArrayList<String>(Arrays.asList(f.list()));

		for(int i=0;i<files.size();i++) {
			System.out.println(files.get(i));
		}
	}

	//---------------------------------------------------------------------------------------------------
	public static void copyDirectory(String srcPath, String destPath) throws Exception
	{
		File srcFolder = new File(srcPath);
		File destFolder = new File(destPath);

		//make sure source exists
		if(!srcFolder.exists()){
			System.out.println("Directory does not exist.");
			
		}
		else{
			try{
				copySubFolder(srcFolder,destFolder);
			}catch(IOException e){
				e.printStackTrace();
				System.exit(0);
			}
		}
		System.out.println("Done");
	}

	// Method required for copying directory
	public static void copySubFolder(File src, File dest)
			throws IOException{
		if(src.isDirectory()){
			//if directory not exists, create it
			if(!dest.exists()){
				dest.mkdir();
				System.out.println("Directory copied from "+src+" to " + dest);
			}
			//list all the directory contents
			String files[] = src.list();
			for (String file : files) {
				//construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				//recursive copy
				copySubFolder(srcFile,destFile);
			}
		}
		else{
			//if file, then copy it
			//Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest); 
			byte[] buffer = new byte[1024];
			int length;
			//copy the file content in bytes 
			while ((length = in.read(buffer)) > 0){
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}
	}


	public static void main(String[] args) {
		String path = null, path2, text,fName;
		int ch;
		Scanner scanner = new Scanner(System.in);

		do
		{
			System.out.println("1. Perform \"mkdir /someName\" function");
			System.out.println("2. Perform \"create /file1\" function");
			System.out.println("3. Perform \"create /some/file1\" function ");
			System.out.println("4. Perform \"write 'some text' /file1\" function");
			System.out.println("5. Perform \"cat /file\" fucntion");
			System.out.println("6. Perform \"cp /file1 /someName/file2\" fucntion ");
			System.out.println("7. Perform \"find file2 (Will return all file name)\" function");
			System.out.println("8. Peform \"find /someName file2\" function");
			System.out.println("9. Perform \"ls /someName\" function ");
			System.out.println("10. Perform \"cp /someName /copyFolder\" function");
			System.out.println(" 0. Exit");
			System.out.print("Enter the choice : ");
			ch=scanner.nextInt();

			switch(ch){
			case 1: 
				// mkdir /someName
				// If only directory is entered it will create directory in Project folder where program is running
				// if full path is specified it create directory in the specified path.
				// Input: Only Directory name or the full valid path.
				// Note it will not create multiple nested directories.
				System.out.println("Enter the directory name or full path with directory name to create : ");

				try {
					path=scanner.next();
					createFolder(path);
				}
				catch (Exception e) {
					System.out.println("Invalid directory name or path ");
				}
				break;

			case 2:
				// create /file
				// Input: Valid path along with file name 
				System.out.println("Enter the File Name : ");

				try{
					path=scanner.next();
					createFile(path);
				}
				catch(Exception e) {
					System.out.println(" Invalid File name or path... Please try again...");
				}
				break;

			case 3:
				// create /someName/file1
				// Input: Valid path with filename and extension 
				System.out.println("Enter the File Name with full path : ");

				try{
					path=scanner.next();
					createFolderWithFile(path);
				}
				catch(Exception e) {
					System.out.println(" Invalid File name or path... Please try again...");
				}
				break;

			case 4:
				// write "Some text" /file1
				// Input: Valid path with filename and contents to be appended
				System.out.println("Enter the full path with File Name : ");

				try{
					path=scanner.next();
					System.out.println("Enter the contents to append to file : ");
					text=scanner.next();
					appendToFile(path, text);
				}
				catch(Exception e) {
					System.out.println(" Unable to append to file... Please try again...");
				}
				break;

			case 5:
				// cat /file1
				// Input: Valid path with filename 
				System.out.println("Enter the full path with File Name : ");

				try{
					path=scanner.next();
					displayFile(path);
				}
				catch(Exception e) {
					System.out.println("Error with file name or path... Please try again...");
				}
				break;

			case 6:
				// cp /file1 /someName/file2
				// Input: Valid path with filename 
				System.out.println("Enter the first file name (along with path) : ");
				path=scanner.next();
				System.out.println("Enter the second file name (along with path) : ");
				path2=scanner.next();
				try{
					copyFiles(path, path2);
				}
				catch(Exception e) {
					System.out.println("Error with file name or path... Please try again...");
				}
				break;

			case 7:
				// find file2 (Should return all found locations for file2)
				// Input: File name to  search
				System.out.println("Enter the File Name : ");
				try{
					fName=scanner.next();
					searchFilesinFullSystem(new File("C:/"),fName);
				}
				catch (Exception e) {
					System.out.println("");
				}
				break;

			case 8:
				// find /someName file2 
				// Input: File path of directory and filename 
				try{
					List<File> list= new ArrayList<File>();
					System.out.println("Enter the path : ");
					path=scanner.next();
					System.out.println("Enter the File Name to search ");
					fName=scanner.next();
					searchFilesWithName(new File(path), list, fName);
					if(!list.isEmpty() ){
						for(int i=0;i<list.size();i++) {
							System.out.println(list.get(i));
						}
					}
					else
					{
						System.out.println(fName+" not found...");
					}
				}
				catch (Exception e) {
					System.out.println(" Error with file name or path.. Please Try Again....");
				}
				break;

			case 9:
				// ls /someName
				// Input: Absolute path to folder
				System.out.println("Enter the Path : ");
				try{
					path=scanner.next();
					listFolderContents(path);
				}
				catch (Exception e) {
					System.out.println("Error with path.. Please Try Again.... ");
				}
				break;
			case 10:
				// cp /someName /copyFolder
				// Input: Source Folder Absolute path and destination folder absolute path

				try{
					System.out.println("Enter source folder Path : ");
					path=scanner.next();
					System.out.println("Enter the destination folder path : ");
					path2=scanner.next();
					copyDirectory(path, path2);
				}
				catch (Exception e) {
					System.out.println("Error with path.. Please Try Again.... ");
				}
				break;
			}

		}while(ch!=0);
	}
}
