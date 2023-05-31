/*
 * package com.blogsite.entity;
 * 
 * import static org.junit.jupiter.api.Assertions.*;
 * 
 * import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
 * import org.junit.jupiter.api.extension.ExtendWith; import
 * org.mockito.junit.jupiter.MockitoExtension;
 * 
 * class BlogsTest { Blogs blog1 = new Blogs(); Blogs blog2 = new Blogs(); Blogs
 * blog3 = new Blogs();
 * 
 * @BeforeEach void setUp() throws Exception { blog1.setBlogname("blogname");
 * blog1.setCategory(Category.FOOD); blog1.setAuthorname("authorname");
 * blog1.setArticle("article"); blog2.setBlogname("blogname");
 * blog2.setCategory(Category.FOOD); blog2.setAuthorname("authorname");
 * blog2.setArticle("article"); blog3.setBlogname("blogname1");
 * blog3.setCategory(Category.FOOD); blog3.setAuthorname("authorname1");
 * blog3.setArticle("article1"); }
 * 
 * @Test void testHashCode() { assertEquals(new Blogs().hashCode(),new
 * Blogs().hashCode()); assertEquals(blog1.hashCode(),blog2.hashCode());
 * assertNotEquals(blog1.hashCode(),blog3.hashCode()); }
 * 
 * @Test void testEqualsObject() { assertTrue(new Blogs().equals(new Blogs()));
 * assertFalse(blog1.equals(blog3)); assertTrue(blog1.equals(blog2));
 * assertFalse(blog2.equals(null)); }
 * 
 * @Test void testToString() { assertEquals(blog1.toString(),blog1.toString());
 * }
 * 
 * 
 * 
 * }
 */