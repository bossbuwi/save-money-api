1. Duplicate the file application.properties on the src/main/resources  
2. Rename the new file as application-local.properties  
3. Modify the properties api.supabase.url and api.supabase.key
4. On the IntelliJ run configuration add -Dspring.profiles.active=local as a VM option.
5. Profit!