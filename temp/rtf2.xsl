<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
{\rtf1\ansi\ansicpg1252\deff0\nouicompat{\fonttbl{\f0\fnil\fcharset0 Calibri;}}
{\*\generator Riched20 10.0.14393}\viewkind4\uc1 
\pard\sa200\sl276\slmult1\f0\fs22\lang9\par

	<xsl:for-each select="/logMessages/logMessage">
      <xsl:value-of select="date"/>
	  \par
      <xsl:value-of select="message"/>
	  \par
    </xsl:for-each>
}






</xsl:template>

</xsl:stylesheet>