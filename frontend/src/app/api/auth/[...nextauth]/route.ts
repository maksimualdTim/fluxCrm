import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import axios from "axios";

export const handler = NextAuth({
  providers: [
    CredentialsProvider({
      name: "Credentials",
      credentials: {
        email: { label: "Email", type: "email" },
        password: { label: "Password", type: "password" },
      },
      async authorize(credentials) {
        try {
          const res = await axios.post(
            `${process.env.KEYCLOAK_ISSUER}/protocol/openid-connect/token`,
            new URLSearchParams({
              client_id: process.env.KEYCLOAK_ID,
              client_secret: process.env.KEYCLOAK_SECRET,
              grant_type: "password",
              username: credentials?.email || "",
              password: credentials?.password || "",
            }),
            { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
          );

          if (res.data.access_token) {
            return { token: res.data.access_token, user: { email: credentials?.email } };
          } else {
            return null;
          }
        } catch (error) {
          console.error("Ошибка авторизации:", error);
          return null;
        }
      },
    }),
  ],
  pages: {
    signIn: "/login"
  },
  callbacks: {
    async jwt({ token, user }) {
      console.log("User");
      console.log(user);
      console.log("token");
      console.log(token);
      
      if (user) {
        token.accessToken = user.token;
      }
      return token;
    },
    async session({ session, token }) {
      console.log("session");
      console.log(session);
      console.log("token");
      console.log(token);
      
      session.accessToken = token.accessToken;
      return session;
    },
  },
  secret: process.env.AUTH_SECRET,
});

export {handler as GET, handler as POST}