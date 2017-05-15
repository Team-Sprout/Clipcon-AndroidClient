package com.sprout.clipcon.server;

import com.sprout.clipcon.service.TopService;


public class ContentsUpload {
    public UploadData uploader;

    public ContentsUpload(String userName, String groupKey) {
        uploader = new UploadData(userName, groupKey);
    }

    public void upload(String type) {
        switch (type) {
            case "text":
                uploader.uploadStringData(TopService.getTextData());
                break;
            case "image":

                break;
        }
    }

    /*
     public void upload() {

		 Object clipboardData = ClipboardController.readClipboard();

		 if (clipboardData instanceof String) {
			 System.out.println("instanceof String");
			 uploader.uploadStringData((String) clipboardData);
	     }
		 else if (clipboardData instanceof Image) {
			 System.out.println("instanceof Image");
			 uploader.uploadCapturedImageData((Image) clipboardData);
	     }
		 else if (clipboardData instanceof ArrayList<?>) {
			 System.out.println("instanceof ArrayList");
			 uploader.uploadMultipartData((ArrayList<String>) clipboardData);
		 }
	 }*/
}