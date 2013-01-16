package com;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.action.Action;
import com.entity.User;

import eFrame.container.EntityContainer;

/**
 * 
 * @author LiangRL
 * @alias E.E.
 */
public class Test {

	/** 扫描这个目录下的文件 */
	final static String resourcePackage = "com";

	static void testDI() throws Exception {
		EntityContainer cc = EntityContainer.getInstance();
		cc.invoke(new String[] { "com.action", "com.service", "com.dao" });
		Action a = (Action) cc.getBean("action");

		User u = new User();
		u.setEmail("123@123.com");
		u.setName("A");
		u.setPassword("111");
		u.setPhone("137");
		u.setComment("aa");
		a.add(u);
	}

	static void testVelocity() {
		Properties p = new Properties();
		String path = new Test().getClass().getResource("/").toString()  
                .replaceAll("^file:/", "")+"templates";  
		
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
		Velocity.init(p);
		VelocityContext context = new VelocityContext();
		context.put("name", new String("Velocity"));
		// 选择要用到的模板
		Template template = Velocity.getTemplate("userList.vm");

		StringWriter sw = new StringWriter();
		// 合并输出
		template.merge(context, sw);		
		System.out.println(sw.toString());
	}

	static void testString(){
		String str = "user.getUserList";
		String[] temp = str.split("\\.");
		System.out.println(temp[0]+"\t"+temp[1]);
	}
	
	public static void main(String[] args) throws Exception {
		
		testString();
	}
}
