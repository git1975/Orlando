/*dataSource {
 pooled = true
 jmxExport = true
 driverClassName = "org.h2.Driver"
 username = "sa"
 password = ""
 }*/
dataSource {
	pooled = true
	dbCreate = "update"
	url = "jdbc:mysql://localhost:3306/orlando"
	driverClassName = "com.mysql.jdbc.Driver"
	username = "admin"
	password = "admin"
}
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = false
	//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
	cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
	singleSession = true // configure OSIV singleSession mode
	flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
	development {
		dataSource { dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
			//url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
		}
	}
	production {
		dataSource {
			pooled = true
			dbCreate = "update"
			url = "jdbc:mysql://mysql80591-env-3937912.jelastic.regruhosting.ru/orlando?useUnicode=yes&characterEncoding=UTF-8"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "dima"
			password = "igogo123"
		}
	}
}
