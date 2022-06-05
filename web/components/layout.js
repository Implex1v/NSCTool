import Head from 'next/head'
import Navbar from "./nav";

export const siteTitle = 'NSCTool'

export default function Layout({children}) {
    return (
        <div className="layout">
            <Head>
                <link rel="icon" href="/favicon.ico"/>
                <meta
                    name="description"
                    content="A website for managing your NSCs of Pen and Paper games. Mainly focused on the german PnP game Das Schwarze Auge (DSA)"
                />
                <meta name="og:title" content={siteTitle}/>
                <meta name="twitter:card" content="summary_large_image"/>
                <title>{siteTitle}</title>
            </Head>
            <Navbar />

            <div className="main container-xl text-light">
                <div className="row">
                    <div className="col-xl-12">
                        <main className="mb-auto">
                            {children}
                        </main>
                    </div>
                </div>

                <footer className="row">
                    <div className="col-md-12">
                        <div className="w-full mx-auto">
                            <p className="text-center">&copy; NSCTool 2022</p>
                        </div>
                    </div>
                </footer>
            </div>
        </div>
    )
}
