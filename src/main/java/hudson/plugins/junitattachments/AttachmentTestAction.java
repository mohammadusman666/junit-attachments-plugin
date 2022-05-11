package hudson.plugins.junitattachments;

import hudson.FilePath;
import hudson.Util;
import hudson.model.DirectoryBrowserSupport;
import hudson.tasks.junit.TestAction;
import hudson.tasks.test.TestObject;
import java.util.List;
import jenkins.model.Jenkins;

public class AttachmentTestAction extends TestAction {

	private final FilePath storage;
	private final List<String> attachments;
	private final TestObject testObject;

	public AttachmentTestAction(TestObject testObject, FilePath storage, List<String> attachments) {
		this.storage = storage;
		this.testObject = testObject;
		this.attachments = attachments;
	}

	public String getDisplayName() {
		return "Attachments";
	}

	public String getIconFileName() {
		return "package.gif";
	}

	public String getUrlName() {
		return "attachments";
	}

	public DirectoryBrowserSupport doDynamic() {
		return new DirectoryBrowserSupport(this, storage, "Attachments", "package.gif", true);
	}

	@Override
	public String annotate(String text) {
		String url = Jenkins.getActiveInstance().getRootUrl()
				+ testObject.getUrl() + "/attachments/";
		for (String attachment : attachments) {
			text = text.replace('/'+attachment, "/<a href=\"" + url + attachment
					+ "\">" + attachment + "</a>");
			text = text.replace("taking screenshot "+attachment, "<a href=\"" + url + attachment
					+ "\"><div style=\"position: relative\"><img width=\"100%\" src=\"" + url + attachment + "\"></img></div></a>");
		}
		text = text.replaceAll("</div></a>&lt;(\\d+\\.?\\d*),(\\d+\\.?\\d*)>",
			"<div style=\"position: absolute; background-color: rgba(255,0,0,.5); height: 20px; width: 20px; border-radius: 50%; top: calc($2% - 10px); left: calc($1% - 10px)\"></div></div></a>");
		return text;
	}

	public List<String> getAttachments() {
		return attachments;
	}

	public TestObject getTestObject() {
		return testObject;
	}

	public static boolean isImageFile(String filename) {
		return filename.matches("(?i).+\\.(gif|jpe?g|png)$");
	}

	public static String getUrl(String filename) {
		return "attachments/" + Util.rawEncode(filename);
	}

}
