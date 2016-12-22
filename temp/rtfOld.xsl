<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text"/>
<xsl:template match="/">
	<xsl:text>{\rtf1</xsl:text>
	<xsl:text> \par</xsl:text>
	<xsl:for-each select="/logMessages/logMessage">
		<xsl:value-of select="date"/>
		<xsl:text>\par</xsl:text>
		<xsl:value-of select="message"/>
		<xsl:text>\par</xsl:text>
	</xsl:for-each>
	<xsl:text>}</xsl:text>
</xsl:template>

</xsl:stylesheet>