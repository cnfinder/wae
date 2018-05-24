package cn.finder.wae.service.rest.common;

public class MediaUtils {

	public static String[] img_mediatypes=new String[]{
		"image/jpeg","image/jpg","image/gif"
	};
	
	public static boolean judgeImageMedia(String partName)
	{
		for(String str:img_mediatypes)
		{
			if(str.indexOf(partName)!=-1)
			{
				return true;
			}
		}
		return false;
	}
}
