// app/layout.tsx

import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Sidebar from "@/components/Sidebar"; // Sidebar 컴포넌트 import (경로에 @ 사용)

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "INSK v2.0 Dashboard",
  description: "AI News Trend Sensing Dashboard",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="flex h-screen bg-gray-800">
          <Sidebar /> {/* 사이드바를 여기에 추가 */}
          <main className="flex-1 p-6 overflow-y-auto">
            {children} {/* 이 부분이 각 페이지의 실제 내용으로 교체됩니다. */}
          </main>
        </div>
      </body>
    </html>
  );
}