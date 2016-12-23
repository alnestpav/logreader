<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
  <html>
  <head>
  <style>
  div {
	word-wrap: break-word;
    white-space: pre-wrap;
    margin-top: -1.5em;
  }
  p {
    font-family: courier new;
    font-size: 14;
    margin-top: -1.5em;
    }


  </style>
  </head>
  <body>
  <div>
    <xsl:for-each select="/logMessages/logMessage">
      <div>
        <p><xsl:value-of select="date"/></p>
        <p><xsl:value-of select="message"/></p>
      </div>
    </xsl:for-each>
  </div>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>