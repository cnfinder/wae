package cn.finder.wae.test;

import org.junit.Test;

public class MongoDBTest {

	@Test
	public void testConnect() {
		try {
			// 连接到 mongodb 服务
		//	Mongo mongo = new Mongo("127.0.0.1", 27017);
			// 根据mongodb数据库的名称获取mongodb对象 ,
			// DB db = mongo.getDB("user");
			// db.getCollectionNames();

			// ============增删改查=============
		/*	MongoTemplate mongoTemplate = new MongoTemplate(mongo, "user");
			Query query = new Query();
			Update update = new Update();*/
			// 增加一条
			// mongoTemplate.insert(new User("lizhi",24),"person");

			// 增加一组数据
			// List<User> users = new ArrayList<User>();
			// users.add(new User("bbb", 22, new Favourite("bbb")));
			// users.add(new User("ccc", 19, new Favourite("ccc")));
			// mongoTemplate.insert(users, "person");

//			 修改数据
//			 query.addCriteria(Criteria.where("name").is("gjj"));
//			 update.set("age", 22);
//			 update.set("favourite.name", "cry");
//			 mongoTemplate.upsert(query, update, User.class, "person");
			
			//多种修改方式
		//	int count = mongoTemplate.getCollection("person").update(new BasicDBObject("name","aaa"), new BasicDBObject("$set",new BasicDBObject("age",21))).getN();
		//	System.out.println(count);
			
			// 查询数据
			//query.addCriteria(Criteria.where("age").is(22).andOperator(Criteria.where("favourite.name").is("cry")));
//			List<BasicDBObject> users = mongoTemplate.find(null,BasicDBObject.class,"person");
//			for (BasicDBObject obj : users) {
//				System.out.println(obj.toString());
//			}
			
			//多种查询方式
//			List<DBObject> users = mongoTemplate.execute("person", new CollectionCallback<List<DBObject>>() {
//				@Override
//				public List<DBObject> doInCollection(DBCollection dbcollection) throws MongoException, DataAccessException {
////					利用json字符串查询
////					String json = "{'age':{'$gt':21}}";
////					BasicDBObject basicDBObject = (BasicDBObject) JSON.parse(json);
////					BasicDBObject basicDBObject = new BasicDBObject("age", new BasicDBObject("$gt",22));
//					
//					Object obj = new Integer(22);
//					BasicDBObject basicDBObject = new BasicDBObject("age",obj);
//					
////					查找不为空的字段（回去除掉不含该字段的数据）
////					BasicDBObject basicDBObject = new BasicDBObject("favourite",null).append("$exist", true);
//					
////					BasicDBObject basicDBObject = new BasicDBObject("favourite",new BasicDBObject("$ne",null));
//					
//					DBCursor cursor = dbcollection.find(basicDBObject);
//					return cursor.toArray();
//				}
//			});
//			for (DBObject obj : users) {
//				System.out.println(obj.toString());
//			}
			
			// 另一种方式查询全部
//			List<DBObject> users = mongoTemplate.execute("person", new CollectionCallback<List<DBObject>>() {
//				@Override
//				public List<DBObject> doInCollection(DBCollection dbcollection) throws MongoException, DataAccessException {
//					DBObject db = new BasicDBObject();
////					db.put("favourite.name", "cry");
//					DBObject field = new BasicDBObject();
//					//0即去除该字段，1显示该字段
//					field.put("name", 1);
//					field.put("age", 1);
//					//field.put("_id", 0);
//					DBCursor cursor = dbcollection.find(db,field).sort(new BasicDBObject("_id", 1));
//					return cursor.toArray();
//				}
//			});
//			for (DBObject obj : users) {
//				System.out.println(obj.toString());
//			}
			//删除(注意数据类型，19为整形)
//			query.addCriteria(Criteria.where("age").is(19));
//			mongoTemplate.remove(query,"person");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
